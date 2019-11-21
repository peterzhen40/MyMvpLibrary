package cn.peterzhen.demo.net;

import java.util.Map;
import java.util.Set;

/**
 * 当前类注释:登录 返回值
 * Author: zhenyanjun
 * Date  : 2017/5/17 15:38
 */

public class LoginResult {

    /**
     * id : 1
     * account : slj
     * password : 1C395A8DCE135849BD73C6DBA3B54809
     * token : qdYVvIrpZX6XNnop22tFQ7DRPfcYz1XihpF05RH4Mn0Ip2yd0KdObA==
     * disabled : false
     * exprired : true
     * locked : false
     * deleted : false
     * realName : 孙玲军
     * compId : 0
     * compName : 广州中爆数字信息科技股份有限公司
     * compCode : 4400000000000000
     * compNature : KF
     * type : 1
     * compNo : 000
     */

    private String id;
    private String account;
    private String password;
    private String token;
    private String realName;
    private String compId;
    private String compName;
    private String compCode; // dwCode企业内部编码    compCode则是公安机关管辖编码,安全检查选择列表项
    private String compNature;// KF 客服或管理员，ST省厅  SJ 市局 FJ 分局  PS 派出所 SQ 社区   QY 企业   区分公安或企业用compnature,
    private String type;  //用户类型  1 系统管理员，2 系统管理员分配的普通用户 3 普通用户分配的普通用户，4 单位管理员
    private String compNo;
    private String dwCode;
    private String areaCode;
    private String jgLb;  //主体类型  //机构类别,0公安机构、10总公司、20品牌企业(广州品牌企业(广州分公司))、30营业部(一级网点)、31分拣中心、32仓储基地、40末端网点、15物流园区、45物流点档、90人员
    private Map<String, String> permissionsMap;

    public String getDwCode() {
        return dwCode;
    }

    public void setDwCode(String dwCode) {
        this.dwCode = dwCode;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getJgLb() {
        return jgLb;
    }

    public void setJgLb(String jgLb) {
        this.jgLb = jgLb;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }


    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getCompId() {
        return compId;
    }

    public void setCompId(String compId) {
        this.compId = compId;
    }

    public String getCompName() {
        return compName;
    }

    public void setCompName(String compName) {
        this.compName = compName;
    }

    public String getCompCode() {
        return compCode;
    }

    public void setCompCode(String compCode) {
        this.compCode = compCode;
    }

    public String getCompNature() {
        return compNature;
    }

    public void setCompNature(String compNature) {
        this.compNature = compNature;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCompNo() {
        return compNo;
    }

    public void setCompNo(String compNo) {
        this.compNo = compNo;
    }

    public Map<String, String> getPermissionsMap() {
        return permissionsMap;
    }

    public void setPermissionsMap(Map<String, String> permissionsMap) {
        this.permissionsMap = permissionsMap;
    }

    public Set<String> getPermissionSet() {
        if (permissionsMap != null) {
            return permissionsMap.keySet();
        } else {
            return null;
        }
    }

    /**
     * 是否为公安或管理员
     */
    public boolean isGa() {
        return ("KF".equals(compNature)
                || "ST".equals(compNature)
                || "SJ".equals(compNature)
                || "FJ".equals(compNature)
                || "PS".equals(compNature)
                || "SQ".equals(compNature));
    }

    public boolean isQy() {
        return "QY".equals(compNature);
    }

    public boolean isSJ() {
        return "SJ".equals(compNature);
    }

    public boolean isFJ() {
        return "FJ".equals(compNature);
    }

    public boolean isPS() {
        return "PS".equals(compNature);
    }

    public boolean isCompAdmin() {
        return isQy() && "4".equals(type);
    }

    /**
     * 管理员
     * @return
     */
    public boolean isAdmin() {
        return "KF".equals(compNature);
    }

    /**
     * 是否从业人员
     * @return
     */
    public boolean isCongyeRenyuan() {
        return "90".equals(jgLb);
    }
}
