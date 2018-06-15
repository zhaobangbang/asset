package com.lans.infrastructure.dao;

import org.hibernate.Session;

public interface HibTransRunnable {
	public void run(Session sess) throws Exception;
}
