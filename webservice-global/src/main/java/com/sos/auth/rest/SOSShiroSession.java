package com.sos.auth.rest;

import java.io.Serializable;

import org.apache.shiro.session.InvalidSessionException;
import org.apache.shiro.session.Session;

import com.sos.auth.rest.SOSShiroCurrentUser;
import com.sos.joc.exceptions.SessionNotExistException;

public class SOSShiroSession {
	private SOSShiroCurrentUser shiroUser;
	private Session shiroSession;

	public SOSShiroSession(SOSShiroCurrentUser shiroUser) {
		super();
		this.shiroUser = shiroUser;
	}

	private void initSession() {
		if (shiroSession == null) {
			shiroSession = shiroUser.getCurrentSubject().getSession(false);
		}
	}

	public void setAttribute(String key, Object value) throws SessionNotExistException {
		try {
			initSession();
			if (shiroSession != null) {
				shiroSession.setAttribute(key, value);
			}
		} catch (InvalidSessionException e1) {
			throw new SessionNotExistException(e1);
		}
	}

	public Object getAttribute(Object key) throws SessionNotExistException {
		initSession();
		Session shiroSession = shiroUser.getCurrentSubject().getSession(false);
		try {
			if (shiroSession != null) {
				return shiroSession.getAttribute(key);
			}
		} catch (InvalidSessionException e1) {
			throw new SessionNotExistException(e1);
		}

		return null;
	}

	public String getStringAttribute(Object key) throws SessionNotExistException {
		initSession();
		try {
			if (shiroSession != null) {
				return (String) shiroSession.getAttribute(key);
			}
		} catch (InvalidSessionException e1) {
			throw new SessionNotExistException(e1);
		}

		return null;
	}

	public Boolean getBooleanAttribute(Object key) throws SessionNotExistException {
		initSession();
		try {
			if (shiroSession != null) {
				return (Boolean) shiroSession.getAttribute(key);
			}
		} catch (InvalidSessionException e1) {
			throw new SessionNotExistException(e1);
		}

		return null;
	}

	public void removeAttribute(Object key) throws SessionNotExistException {
		initSession();
		try {
			if (shiroSession != null) {
				shiroSession.removeAttribute(key);
			}
		} catch (InvalidSessionException e1) {
			throw new SessionNotExistException(e1);
		}

	}

	public void stop() throws SessionNotExistException {
		initSession();
		try {
			if (shiroSession != null) {
				shiroSession.stop();
			}
		} catch (InvalidSessionException e1) {
			throw new SessionNotExistException(e1);
		}

	}

	public long getTimeout() throws SessionNotExistException {
		initSession();
		try {
			if (shiroSession != null) {
				return shiroSession.getTimeout();
			} else {
				return 0L;
			}
		} catch (InvalidSessionException e1) {
			throw new SessionNotExistException(e1);
		}

	}

	public Serializable getId() throws SessionNotExistException {
		initSession();
		try {
			if (shiroSession != null) {
				return shiroSession.getId();
			} else {
				return null;
			}
		} catch (InvalidSessionException e1) {
			throw new SessionNotExistException(e1);
		}

	}

	public void touch() throws SessionNotExistException {
		initSession();
		try {
			if (shiroSession != null) {
				shiroSession.touch();
			}
		} catch (InvalidSessionException e1) {
			throw new SessionNotExistException(e1);
		}

	}

}
