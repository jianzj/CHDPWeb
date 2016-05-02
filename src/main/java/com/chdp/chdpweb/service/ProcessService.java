package com.chdp.chdpweb.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.chdp.chdpweb.dao.ProcessDao;
import com.chdp.chdpweb.Constants;
import com.chdp.chdpweb.bean.Process;

@Repository
public class ProcessService {

	@Autowired
	private ProcessDao proDao;

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
}
