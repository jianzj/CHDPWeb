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

import com.chdp.chdpweb.dao.PrescriptionDao;
import com.chdp.chdpweb.dao.ProcessDao;
import com.chdp.chdpweb.Constants;
import com.chdp.chdpweb.bean.AppResult;
import com.chdp.chdpweb.bean.Prescription;
import com.chdp.chdpweb.bean.Process;
import com.chdp.chdpweb.bean.User;

@Repository
public class ProcessService {

    @Autowired
    private ProcessDao proDao;

    @Autowired
    private PrescriptionDao prsDao;

    @Autowired
    private DataSourceTransactionManager transactionManager;

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
	
	public List<Process> getProcessChainWithProcessId(int id){
		List<Process> list = new ArrayList<Process>();
		
		try{
			Process prs = proDao.getProcessesById(id);
			if (prs.getPrevious_process_id() > 0){
				list = this.getProcessChainWithProcessId(prs.getPrevious_process_id());
			}
			list.add(prs);
		}catch (Exception e){
			return list;
		}
		return list;
	}
	
	public boolean updateProcessTime(Process process){
		try{
			proDao.refreshProcessTime(process);
			return true;
		} catch (Exception e){
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
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String currentTime = df.format(new Date());
            User user = (User) SecurityUtils.getSubject().getSession().getAttribute("user");
            proDao.finishProcess(procId, currentTime, user.getId());

            try {
                Process proc = new Process();
                proc.setBegin(currentTime);
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
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String currentTime = df.format(new Date());
            User user = (User) SecurityUtils.getSubject().getSession().getAttribute("user");
            if (forwardTo != Constants.DECOCT) {
                if (machineId == 0)
                    proDao.finishProcess(procId, currentTime, user.getId());
                else
                    proDao.finishProcessWithMachine(procId, currentTime, user.getId(), machineId);
            } else
                proDao.startProcess(procId, currentTime, user.getId());

            try {
                if (forwardTo != Constants.SHIP) {
                    Process proc = new Process();
                    if (forwardTo != Constants.SOAK && forwardTo != Constants.DECOCT)
                        proc.setBegin(currentTime);
                    proc.setProcess_type(forwardTo);
                    proc.setPrescription_id(prsId);
                    proc.setPrevious_process_id(procId);
                    proc.setUser_id(user.getId());
                    proDao.createProcess(proc);
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
                }else{
                    try {
                        Prescription prs = new Prescription();
                        prs.setId(prsId);
                        prs.setProcess(forwardTo);
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

    public AppResult backwardProcess(int prsId, int procId, int backTo, int type, String reason) {
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        TransactionStatus status = transactionManager.getTransaction(def);
        AppResult result = new AppResult();
        try {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String currentTime = df.format(new Date());
            User user = (User) SecurityUtils.getSubject().getSession().getAttribute("user");
            proDao.cancelProcess(procId, currentTime, user.getId(), type, reason);

            try {
                Process proc = new Process();
                if (backTo != Constants.SOAK && backTo != Constants.DECOCT)
                    proc.setBegin(currentTime);
                proc.setProcess_type(backTo);
                proc.setPrescription_id(prsId);
                proc.setPrevious_process_id(procId);
                proc.setUser_id(user.getId());
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
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String currentTime = df.format(new Date());
            User user = (User) SecurityUtils.getSubject().getSession().getAttribute("user");
            proDao.startProcess(procId, currentTime, user.getId());
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
                result.setErrorMsg("浸泡时间小于要求，请在" + Math.ceil((Constants.SOAK_TIME - now + begin) / 1000 / 60.0) + "分钟后再进行煎煮");
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

    public int getProcIdwithPrsandStatus(int prsId, int process_type){
    	try{
    		return proDao.getProcIdwithPrsandStatus(prsId, process_type);
    	} catch (Exception e){
    		return -1;
    	}
    }
    
	public int getProcessTypebyUserAuth(int userAuth){
		
		if ((userAuth&512) != 0){
			return Constants.RECEIVE;
		}else if ((userAuth&256) != 0){
			return Constants.CHECK;
		}else if((userAuth&128) != 0){
			return Constants.MIX;
		}else if((userAuth&64) != 0){
			return Constants.MIXCHECK;
		}else if((userAuth&32) != 0){
			return Constants.SOAK;
		}else if((userAuth&16) != 0){
			return Constants.DECOCT;
		}else if((userAuth&8) != 0){
			return Constants.POUR;
		}else if((userAuth&4) != 0){
			return Constants.CLEAN;
		}else if((userAuth&2) != 0){
			return Constants.PACKAGE;
		}else if((userAuth&1) != 0){
			return Constants.SHIP;
		}
		
		return Constants.RECEIVE;
	}
}
