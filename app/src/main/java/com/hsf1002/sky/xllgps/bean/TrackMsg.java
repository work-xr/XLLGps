package com.hsf1002.sky.xllgps.bean;

import java.util.List;

/**
 * Created by hefeng on 18-6-14.
 */

public class TrackMsg {

    private class Data
    {
        private Ext ext;
        private String ip;
        private List<Ports> ports;
        private String sn;
        private String tipUrl;
        private int updateFlag;
        private String updateUrl;

        private class Ext
        {

        }

        private class Ports
        {
            private String apply;
            private String protocol;
            private String value;

            public String getApply() {
                return apply;
            }

            public void setApply(String apply) {
                this.apply = apply;
            }

            public String getProtocol() {
                return protocol;
            }

            public void setProtocol(String protocol) {
                this.protocol = protocol;
            }

            public String getValue() {
                return value;
            }

            public void setValue(String value) {
                this.value = value;
            }

            @Override
            public String toString() {
                return apply + protocol + value;
            }
        }

        public Ext getExt() {
            return ext;
        }

        public void setExt(Ext ext) {
            this.ext = ext;
        }

        public String getIp() {
            return ip;
        }

        public void setIp(String ip) {
            this.ip = ip;
        }

        public List<Ports> getPorts() {
            return ports;
        }

        public void setPorts(List<Ports> ports) {
            this.ports = ports;
        }

        public String getSn() {
            return sn;
        }

        public void setSn(String sn) {
            this.sn = sn;
        }

        public String getTipUrl() {
            return tipUrl;
        }

        public void setTipUrl(String tipUrl) {
            this.tipUrl = tipUrl;
        }

        public int getUpdateFlag() {
            return updateFlag;
        }

        public void setUpdateFlag(int updateFlag) {
            this.updateFlag = updateFlag;
        }

        public String getUpdateUrl() {
            return updateUrl;
        }

        public void setUpdateUrl(String updateUrl) {
            this.updateUrl = updateUrl;
        }
    }

    private Data data;
    private String message;
    private int retCode;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getRetCode() {
        return retCode;
    }

    public void setRetCode(int retCode) {
        this.retCode = retCode;
    }
}
