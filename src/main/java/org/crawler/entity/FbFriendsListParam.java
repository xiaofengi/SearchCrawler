package org.crawler.entity;

public class FbFriendsListParam {
    private String m_sess;
    private String fb_dtsg;
    private String __dyn;
    private String __req;
    private String __ajax__;
    private Long __user;

    public FbFriendsListParam() {
    }

    public FbFriendsListParam(String m_sess, String fb_dtsg, String __dyn, String __req, String __ajax__, Long __user) {
        this.m_sess = m_sess;
        this.fb_dtsg = fb_dtsg;
        this.__dyn = __dyn;
        this.__req = __req;
        this.__ajax__ = __ajax__;
        this.__user = __user;
    }

    public String getM_sess() {
        return m_sess;
    }

    public void setM_sess(String m_sess) {
        this.m_sess = m_sess;
    }

    public String getFb_dtsg() {
        return fb_dtsg;
    }

    public void setFb_dtsg(String fb_dtsg) {
        this.fb_dtsg = fb_dtsg;
    }

    public String get__dyn() {
        return __dyn;
    }

    public void set__dyn(String __dyn) {
        this.__dyn = __dyn;
    }

    public String get__req() {
        return __req;
    }

    public void set__req(String __req) {
        this.__req = __req;
    }

    public String get__ajax__() {
        return __ajax__;
    }

    public void set__ajax__(String __ajax__) {
        this.__ajax__ = __ajax__;
    }

    public Long get__user() {
        return __user;
    }

    public void set__user(Long __user) {
        this.__user = __user;
    }
}
