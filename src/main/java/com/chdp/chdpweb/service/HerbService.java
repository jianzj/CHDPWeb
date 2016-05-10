package com.chdp.chdpweb.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.chdp.chdpweb.Constants;
import com.chdp.chdpweb.bean.Herb;
import com.chdp.chdpweb.dao.HerbDao;
import com.github.pagehelper.PageHelper;

@Repository
public class HerbService {

	@Autowired
	private HerbDao herbDao;

	public List<Herb> getHerbList(int pageNum) {
		PageHelper.startPage(pageNum, Constants.PAGE_SIZE);
		try {
			return herbDao.getHerbs();
		} catch (Exception e) {
			return new ArrayList<Herb>();
		}

	}

	public List<Herb> getHerbListByType(int type) {
		try {
			return herbDao.getHerbsByType(type);
		} catch (Exception e) {
			return new ArrayList<Herb>();
		}

	}

	public List<Herb> getHerbs() {
		try {
			return herbDao.getHerbs();
		} catch (Exception e) {
			return new ArrayList<Herb>();
		}
	}

	public boolean addHerb(Herb herb) {
		try {
			herbDao.createHerb(herb);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public boolean deleteHerb(int herbId) {
		try {
			herbDao.deleteHerb(herbId);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public boolean doesHerbExist(Herb herb) {
		try {
			if (herbDao.doesHerbExist(herb) > 0) {
				return true;
			}
			return false;
		} catch (Exception e) {
			return false;
		}
	}

}
