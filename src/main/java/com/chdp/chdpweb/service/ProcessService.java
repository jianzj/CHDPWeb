package com.chdp.chdpweb.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.chdp.chdpweb.dao.OrderDao;
import com.chdp.chdpweb.dao.PrescriptionDao;
import com.chdp.chdpweb.dao.ProcessDao;
import com.chdp.chdpweb.dao.UserDao;
import com.chdp.chdpweb.Constants;
import com.chdp.chdpweb.bean.AppResult;
import com.chdp.chdpweb.bean.Prescription;
import com.chdp.chdpweb.bean.Process;
import com.chdp.chdpweb.bean.User;
import com.chdp.chdpweb.bean.Node;
import com.chdp.chdpweb.bean.Order;
import com.chdp.chdpweb.common.Utils;

@Repository
public class ProcessService {

	@Autowired
	private ProcessDao proDao;

	@Autowired
	private PrescriptionDao prsDao;

	@Autowired
	private DataSourceTransactionManager transactionManager;

	@Autowired
	private OrderDao orderDao;
	@Autowired
	private UserDao userDao;

	public int createProccess(Process newProcess) {
		try {
			return proDao.createProcess(newProcess);
		} catch (Exception e) {
			return -1;
		}
	}

	public boolean deleteProcess(int processId) {
		try {
			proDao.deleteProcess(processId);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public int getProcessIdwithProcess(Process process) {
		try {
			return proDao.getProcessIDwithProcess(process);
		} catch (Exception e) {
			return -1;
		}
	}

	public List<Process> getProcessListwithPrsID(int prsId) {
		try {
			return proDao.getProcessesByPrsID(prsId);
		} catch (Exception e) {
			return new ArrayList<Process>();
		}
	}

	public List<Process> getProcessChainWithProcessId(int id) {
		List<Process> list = new ArrayList<Process>();
		try {
			Process prs = proDao.getProcessesById(id);
			if (prs.getPrevious_process_id() > 0) {
				list = this.getProcessChainWithProcessId(prs.getPrevious_process_id());
			}
			list.add(prs);
		} catch (Exception e) {
			return list;
		}
		return list;
	}

	public boolean updateProcessTime(Process process) {
		try {
			proDao.refreshProcessTime(process);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public String getProcessName(int process) {
		if (process == Constants.RECEIVE) {
			return "接方";
		} else if (process == Constants.PACKAGE) {
			return "包装";
		} else {
			return "未知状态";
		}
	}

	public Process getProcessById(int id) {
		try {
			return proDao.getProcessesById(id);
		} catch (Exception e) {
			return null;
		}
	}

	public List<Process> getPresentPreviousProcess(int id) {
		List<Process> list = new ArrayList<Process>();
		try {
			Process present = proDao.getPrescriptionPresentProcess(id);
			list.add(present);
			list.add(proDao.getProcessesById(present.getPrevious_process_id()));
		} catch (Exception e) {
			return list;
		}
		return list;
	}

	public AppResult forwardProcess(int prsId, int procId, Prescription prescription) {
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		TransactionStatus status = transactionManager.getTransaction(def);
		AppResult result = new AppResult();
		try {
			User user = (User) SecurityUtils.getSubject().getSession().getAttribute("user");
			proDao.finishProcess(procId, Utils.getCurrentDateAndTime(), user.getId());

			try {
				Process proc = new Process();
				proc.setProcess_type(Constants.MIX);
				proc.setPrescription_id(prsId);
				proc.setPrevious_process_id(procId);
				proc.setUser_id(user.getId());
				proDao.createProcess(proc);

				try {
					prescription.setProcess(Constants.MIX);
					prescription.setProcess_id(proc.getId());

					prsDao.updatePrescription(prescription);
				} catch (Exception e) {
					transactionManager.rollback(status);
					result.setErrorMsg("更新处方信息失败");
					result.setSuccess(false);

					return result;
				}
			} catch (Exception e) {
				transactionManager.rollback(status);
				result.setErrorMsg("新建调配流程失败");
				result.setSuccess(false);

				return result;
			}
		} catch (Exception e) {
			transactionManager.rollback(status);
			result.setErrorMsg("更新审方流程失败");
			result.setSuccess(false);

			return result;
		}

		transactionManager.commit(status);
		result.setSuccess(true);
		return result;
	}

	public AppResult forwardProcess(int prsId, int procId, int forwardTo, int machineId) {
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		TransactionStatus status = transactionManager.getTransaction(def);
		AppResult result = new AppResult();
		try {
			User user = (User) SecurityUtils.getSubject().getSession().getAttribute("user");
			if (forwardTo != Constants.DECOCT) {
				if (machineId == 0)
					proDao.finishProcess(procId, Utils.getCurrentDateAndTime(), user.getId());
				else
					proDao.finishProcessWithMachine(procId, Utils.getCurrentDateAndTime(), user.getId(), machineId);
			} else
				proDao.startProcess(procId, Utils.getCurrentDateAndTime(), user.getId());

			try {
				if (forwardTo != Constants.SHIP) {
					Process proc = new Process();
					if (forwardTo != Constants.SOAK && forwardTo != Constants.DECOCT)
						proc.setBegin(Utils.getCurrentDateAndTime());
					proc.setProcess_type(forwardTo);
					proc.setPrescription_id(prsId);
					proc.setPrevious_process_id(procId);
					proc.setUser_id(user.getId());
					if (forwardTo == Constants.CLEAN) {
						proc.setMachine_id(machineId);
						proDao.createProcessWithMachine(proc);
					} else {
						proDao.createProcess(proc);
					}

					try {
						Prescription prs = new Prescription();
						prs.setId(prsId);
						prs.setProcess(forwardTo);
						prs.setProcess_id(proc.getId());
						prsDao.updatePrescriptionProcess(prs);
					} catch (Exception e) {
						transactionManager.rollback(status);
						result.setErrorMsg("更新处方信息失败");
						result.setSuccess(false);

						return result;
					}
				} else {
					try {
						Prescription prs = prsDao.getPrescriptionByID(prsId);
						prs.setId(prsId);
						prs.setProcess(forwardTo);
						if (prs.getOrder_id() != 0)
							prs.setProcess_id(prs.getOrder_id());
						else
							prs.setProcess_id(-1);
						prsDao.updatePrescriptionProcess(prs);
					} catch (Exception e) {
						transactionManager.rollback(status);
						result.setErrorMsg("更新处方信息失败");
						result.setSuccess(false);

						return result;
					}
				}
			} catch (Exception e) {
				transactionManager.rollback(status);
				result.setErrorMsg("新建" + Constants.getProcessName(forwardTo) + "流程失败");
				result.setSuccess(false);

				return result;
			}
		} catch (Exception e) {
			transactionManager.rollback(status);
			result.setErrorMsg("更新" + Constants.getProcessName(forwardTo - 1) + "流程失败");
			result.setSuccess(false);

			return result;
		}

		transactionManager.commit(status);
		result.setSuccess(true);
		return result;
	}

	public AppResult backwardProcess(int prsId, int procId, int backTo, int type, String reason, int machineId) {
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		TransactionStatus status = transactionManager.getTransaction(def);
		AppResult result = new AppResult();
		try {
			User user = (User) SecurityUtils.getSubject().getSession().getAttribute("user");
			if (machineId == 0)
				proDao.cancelProcess(procId, Utils.getCurrentDateAndTime(), user.getId(), type, reason);
			else
				proDao.cancelProcessWithMachine(procId, Utils.getCurrentDateAndTime(), user.getId(), type, reason, machineId);
			
			try {
				Process proc = new Process();
				if (backTo != Constants.SOAK && backTo != Constants.DECOCT)
					proc.setBegin(Utils.getCurrentDateAndTime());
				proc.setProcess_type(backTo);
				proc.setPrescription_id(prsId);
				proc.setPrevious_process_id(procId);
				proc.setUser_id(user.getId());
				proc.setMachine_id(machineId);
				proDao.createProcess(proc);

				try {
					Prescription prs = new Prescription();
					prs.setId(prsId);
					prs.setProcess(backTo);
					prs.setProcess_id(proc.getId());
					prsDao.updatePrescriptionProcess(prs);
				} catch (Exception e) {
					transactionManager.rollback(status);
					result.setErrorMsg("更新处方信息失败");
					result.setSuccess(false);

					return result;
				}
			} catch (Exception e) {
				transactionManager.rollback(status);
				result.setErrorMsg("新建" + Constants.getProcessName(backTo) + "流程失败");
				result.setSuccess(false);

				return result;
			}
		} catch (Exception e) {
			transactionManager.rollback(status);
			result.setErrorMsg("更新" + Constants.getProcessName(backTo + 1) + "流程失败");
			result.setSuccess(false);

			return result;
		}

		transactionManager.commit(status);
		result.setSuccess(true);
		return result;
	}

	public AppResult startProcess(int procId, int proc) {
		AppResult result = new AppResult();
		try {
			User user = (User) SecurityUtils.getSubject().getSession().getAttribute("user");
			proDao.startProcess(procId, Utils.getCurrentDateAndTime(), user.getId());
			result.setSuccess(true);
			return result;
		} catch (Exception e) {
			result.setErrorMsg("更新" + Constants.getProcessName(proc) + "流程失败");
			result.setSuccess(false);
			return result;
		}
	}

	public AppResult middleProcess(int procId) {
		AppResult result = new AppResult();
		try {
			proDao.middleProcess(procId, Utils.getCurrentDateAndTime());
			result.setSuccess(true);
			return result;
		} catch (Exception e) {
			result.setErrorMsg("更新保温开始时间失败");
			result.setSuccess(false);
			return result;
		}
	}

	public AppResult startProcessWithMachine(int procId, int proc, int machineId) {
		AppResult result = new AppResult();
		try {
			User user = (User) SecurityUtils.getSubject().getSession().getAttribute("user");
			proDao.startProcessWithMachine(procId, Utils.getCurrentDateAndTime(), user.getId(), machineId);
			result.setSuccess(true);
			return result;
		} catch (Exception e) {
			result.setErrorMsg("更新" + Constants.getProcessName(proc) + "流程失败");
			result.setSuccess(false);
			return result;
		}
	}

	public AppResult checkAndFinish(int procId) {
		AppResult result = new AppResult();

		try {
			Process proc = proDao.getProcessesById(procId);

			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			long now = System.currentTimeMillis();
			long begin = df.parse(proc.getBegin()).getTime();
			if (now - begin < Constants.SOAK_TIME) {
				result.setErrorMsg(
						"浸泡时间小于要求，请在" + Math.ceil((Constants.SOAK_TIME - now + begin) / 1000 / 60.0) + "分钟后再进行煎煮");
				result.setSuccess(false);
				return result;
			}

			try {
				String currentTime = df.format(new Date());
				proDao.finishProcess(procId, currentTime, proc.getUser_id());
				result.setSuccess(true);
				return result;
			} catch (Exception e) {
				result.setErrorMsg("检查浸泡时间失败");
				result.setSuccess(false);
				return result;
			}

		} catch (Exception e) {
			result.setErrorMsg("检查浸泡时间失败");
			result.setSuccess(false);
			return result;
		}
	}

	public int getProcIdwithPrsandStatus(int prsId, int process_type) {
		try {
			return proDao.getProcIdwithPrsandStatus(prsId, process_type);
		} catch (Exception e) {
			return -1;
		}
	}

	// 获取当前订单状态的信息
	public String getPhaseNamewithProcess(Prescription prs) {

		try {
			Process proc = null;
			switch (prs.getProcess()) {
			case Constants.RECEIVE:
				return "正在接方";
			case Constants.MIX:
				proc = proDao.getProcessesById(prs.getProcess_id());
				if (proc.getBegin() == null) {
					return "等待调配";
				} else {
					return "正在调配";
				}
			case Constants.SHIP:
				if (prs.getProcess_id() == -1) {
					return "包装完成";
				} else {
					return "等待出库";
				}
			case Constants.FINISH:
				return "处方完成";
			case Constants.DECOCT:
				proc = proDao.getProcessesById(prs.getProcess_id());
				Process previous = proDao.getProcessesById(proc.getPrevious_process_id());
				if (proc.getBegin() == null) {
					if (previous.getFinish() == null)
						return "正在浸泡";
					else
						return "浸泡完成";
				} else {
					return "正在煎煮";
				}
			case Constants.CLEAN:
				return "正在清场";
			default:
				return Constants.getProcessName(prs.getProcess() - 1) + "完成";
			}
		} catch (Exception e) {
			return "未知状态";
		}

	}

	// 获取一个订单的所有节点状态
	public List<Node> getPrsWorkFlowNods(Prescription prs) {
		try {
			List<Node> nodeList = new ArrayList<Node>();

			List<Process> processList = new ArrayList<Process>();
			if (prs.getProcess() < Constants.SHIP) {
				processList = this.getProcessChainWithProcessId(prs.getProcess_id());
			} else {
				Process temp = proDao.getProcesswithPrsIdandProcess(prs.getId(), Constants.PACKAGE);
				processList = this.getProcessChainWithProcessId(temp.getId());
			}

			for (Process process : processList) {
				Node node = new Node();
				node.setResolvedBy(process.getUser_name());
				node.setNodeId(process.getId());
				node.setNodeType(process.getProcess_type());
				node.setNodeTypeName(Utils.getProcessName(process.getProcess_type()));
				node.setStartTime(process.getBegin());
				node.setSpecialDisplay(false);
				node.setErrorStatus(process.getError_type());
				node.setErrorMsg(process.getError_msg());
				if (process.getProcess_type() == Constants.DECOCT || process.getProcess_type() == Constants.SOAK
						|| process.getProcess_type() == Constants.CLEAN || process.getProcess_type() == Constants.SHIP
						|| process.getProcess_type() == Constants.MIX) {
					node.setSpecialDisplay(true);
				}
				if (process.getBegin() == null && process.getFinish() == null) {
					node.setStatus(0);
				} else if (process.getFinish() == null) {
					node.setStatus(1);
				} else {
					node.setStatus(2);
					node.setMiddleTime(process.getMiddle());
					node.setEndTime(process.getFinish());
					if (process.getProcess_type() == Constants.DECOCT) {
						if (node.getMiddleTime() == null) {
							node.setDecoctTime(Utils.getDecoctTime(node.getStartTime(), node.getEndTime(),
									prs.getClass_of_medicines()));
							node.setHeatTime(Utils.getHeatTime(node.getEndTime(), prs.getClass_of_medicines()));
						} else {
							node.setDecoctTime(Utils.getIntervalTime(node.getStartTime(), node.getMiddleTime()));
							node.setHeatTime(Utils.getIntervalTime(node.getMiddleTime(), node.getEndTime()));
						}
						node.setMachineName(process.getMachine_name());
					} else if (process.getProcess_type() == Constants.POUR) {
						node.setMachineName(process.getMachine_name());
					}
				}
				nodeList.add(node);
			}

			Node shipNode = new Node();
			if (prs.getProcess() >= Constants.SHIP) {
				shipNode.setNodeId(prs.getProcess_id());
				shipNode.setNodeType(Constants.SHIP);
				shipNode.setNodeTypeName(Utils.getProcessName(Constants.SHIP));
				shipNode.setSpecialDisplay(true);
				shipNode.setErrorStatus(0);
				shipNode.setStatus(1);
				if (prs.getProcess() == Constants.FINISH) {
					shipNode.setStatus(2);
				}
				if (prs.getProcess_id() == -1) {
					shipNode.setOrderStatus("等待打印");
				} else {
					Order order = orderDao.getOrderById(prs.getProcess_id());
					shipNode.setStartTime(order.getCreate_time());
					if (order.getStatus() == 1) {
						shipNode.setOrderStatus("等待出库");
					} else {
						shipNode.setOrderStatus("已出库");
						shipNode.setEndTime(order.getOutbound_time());
					}
					shipNode.setResolvedBy(userDao.getUserById(order.getCreate_user_id()).getName());
				}
				nodeList.add(shipNode);
			}

			// 统计订单中所有未完成部分
			for (int i = prs.getProcess() + 1; i < Constants.FINISH; i++) {
				Node node = new Node();
				node.setStatus(0);
				node.setNodeTypeName(Utils.getProcessName(i));
				nodeList.add(node);
			}
			return nodeList;
		} catch (Exception e) {
			return new ArrayList<Node>();
		}
	}

	public int getLastestDecoctMachine(int prsId) {
		return proDao.getProcesswithPrsIdandProcess(prsId, Constants.DECOCT).getMachine_id();
	}
}
