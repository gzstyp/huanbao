package com.fwtai.service.core;

import com.fwtai.bean.PageFormData;
import com.fwtai.bean.SysUser;
import com.fwtai.config.ConfigFile;
import com.fwtai.config.LocalUserId;
import com.fwtai.controller.core.UserDao;
import com.fwtai.entity.User;
import com.fwtai.service.DataService;
import com.fwtai.tool.ToolClient;
import com.fwtai.tool.ToolJWT;
import com.fwtai.tool.ToolString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * 用户账号
 * @作者 田应平
 * @版本 v1.0
 * @创建时间 2020-03-13 17:24
 * @QQ号码 444141300
 * @Email service@dwlai.com
 * @官网 http://www.fwtai.com
*/
@Service
public class UserService extends DataService{

    @Resource
    private UserDao userDao;

    @Autowired
    private BCryptPasswordEncoder passworder;

    private final String SUPER_KEY = "ffffffffddf9f1ffffffffff88888888";

    // 仅仅获取用户userId的角色和权限
    public List<String> getRolePermissions(final String userId,final String url){
        final HashMap<String,String> params = new HashMap<String,String>(2);
        params.put("url",url);
        params.put("userId",userId);
        return userDao.getRolePermissions(params);
    }

    /**
     * 通过userName查询用户信息,用户登录
     * @param username
     * @作者 田应平
     * @QQ 444141300
     * @创建时间 2020/5/1 0:54
     */
    public SysUser getUserByUserName(final String username){
        return userDao.getUserByUserName(username);
    }

    /**
     * 通过userId查询用户的全部角色和权限的信息
     * @param userId
     * @作者 田应平
     * @QQ 444141300
     * @创建时间 2020/5/1 0:53
     */
    public SysUser getUserById(final String userId){
        return userDao.getUserById(userId);
    }

    public String add(final PageFormData pageFormData){
        final String p_user_name = "user_name";
        final String p_user_password = "user_password";
        final String p_verify_password = "verify_password";
        final String validate = ToolClient.validateField(pageFormData,p_user_name,p_user_password,p_verify_password);
        if(validate != null)return validate;
        final String user_name = pageFormData.getString(p_user_name);
        final String password = pageFormData.getString(p_user_password);
        final String verify = pageFormData.getString(p_verify_password);
        if(!password.equals(verify)){
            return ToolClient.createJson(ConfigFile.code199,"输入的两次密码不一致");
        }
        final String exist = userDao.queryExistByUser(user_name);
        if(exist != null){
            return ToolClient.createJson(ConfigFile.code199,user_name+"用户账号已存在,换个试试");
        }
        pageFormData.remove(p_user_password);
        pageFormData.remove(p_verify_password);
        pageFormData.put("kid",ToolString.getIdsChar32());
        pageFormData.put("password",passworder.encode(password));
        final int rows = userDao.add(pageFormData);
        return ToolClient.executeRows(rows);
    }

    public String edit(final PageFormData pageFormData){
        final String p_user_name = "user_name";
        final String p_user_password = "user_password";
        final String p_verify_password = "verify_password";
        final String p_kid = "kid";
        final String validate = ToolClient.validateField(pageFormData,p_user_name,p_user_password,p_verify_password,p_kid);
        if(validate != null)return validate;
        final String user_name = pageFormData.getString(p_user_name);
        final String password = pageFormData.getString(p_user_password);
        final String verify = pageFormData.getString(p_verify_password);
        if(!password.equals(verify)){
            return ToolClient.createJson(ConfigFile.code199,"输入的两次密码不一致");
        }
        final String exist = userDao.queryExistByUser(user_name);
        if(exist == null){
            return ToolClient.createJson(ConfigFile.code199,user_name+"用户账号已不存在");
        }
        pageFormData.remove(p_user_password);
        pageFormData.remove(p_user_name);
        pageFormData.remove(p_verify_password);
        pageFormData.put("password",passworder.encode(password));
        final int rows = userDao.edit(pageFormData);
        return ToolClient.executeRows(rows);
    }

    public String getAllotRole(final PageFormData pageFormData){
        final String loginId = LocalUserId.get();
        final String loginUser = userDao.queryExistById(loginId);
        final String userId = pageFormData.getString("userId");
        if(ConfigFile.KEY_SUPER.equals(loginUser)){
            return ToolClient.queryJson(userDao.getSuperAllotRole(userId));
        }else{
            return ToolClient.queryJson(userDao.getUserIdAllotRole(loginId,userId));
        }
    }

    public User queryUser(final String userName){
        return userDao.queryUser(userName);
    }

    public void updateErrors(final String userName){
        userDao.updateErrors(userName);
    }

    public void updateLoginTime(final String userName){
        userDao.updateLoginTime(userName);
    }

    public String delById(final PageFormData pageFormData){
        final String p_kid = "id";
        final String validate = ToolClient.validateField(pageFormData,p_kid);
        if(validate != null)return validate;
        final String kid = pageFormData.getString(p_kid);
        if(SUPER_KEY.equals(kid)){
            return ToolClient.createJson(ConfigFile.code204,ConfigFile.KEY_SUPER+"账号不能删除");
        }
        final String user_name = userDao.queryExistById(kid);//查询是否存在
        if(user_name == null){
            return ToolClient.createJson(ConfigFile.code199,"用户账号已不存在");
        }
        final int rows = userDao.delById(kid);
        return ToolClient.executeRows(rows);
    }

    //批量分配角色
    public String saveAllotRole(final PageFormData pageFormData){
        final String p_type = "type";
        final String validate = ToolClient.validateField(pageFormData,p_type);
        if(validate != null)return validate;
        final String fieldInteger = ToolClient.validateInteger(pageFormData,p_type);
        if(fieldInteger != null)return fieldInteger;
        final int type = pageFormData.getInteger(p_type);
        if(type == 1){
            final String userId = pageFormData.getString("userId");
            final String kids = pageFormData.getString("kids");
            if(userId == null){
                return ToolClient.jsonValidateField();
            }
            final String exist_key = userDao.queryExistById(userId);//查询是否存在
            if(exist_key == null){
                return ToolClient.createJson(ConfigFile.code199,"用户账号已不存在");
            }
            final ArrayList<String> listRoles = ToolString.keysToList(kids);
            if(listRoles == null || listRoles.size() <= 0){
                final int rows = userDao.delAllotRole(userId);//清空
                return ToolClient.executeRows(rows);
            }else{
                final ArrayList<HashMap<String,String>> lists = new ArrayList<HashMap<String,String>>();
                final Iterator<String> iterator = listRoles.iterator();
                while(iterator.hasNext()){
                    final HashMap<String,String> map = new HashMap<String,String>();
                    map.put("role_id",iterator.next());
                    map.put("user_id",userId);
                    lists.add(map);
                }
                final int rows = userDao.saveAllotRole(userId,lists);//清空之前的角色,重新分配角色
                return ToolClient.executeRows(rows);
            }
        }
        if(type == 2){
            final String userIds = pageFormData.getString("userIds");
            final String kids = pageFormData.getString("kids");
            if(userIds == null){
                return ToolClient.createJson(ConfigFile.code199,"请选择要分配角色的账号");
            }
            final ArrayList<String> listUserIds = ToolString.keysToList(userIds);
            final ArrayList<String> listRoles = ToolString.keysToList(kids);
            if(listRoles == null || listRoles.size() <= 0){
                final int rows = userDao.delBatchUserRole(listUserIds);
                return ToolClient.executeRows(rows);
            }else{
                final ArrayList<HashMap<String,String>> listMaps = new ArrayList<HashMap<String,String>>();
                final Iterator<String> iteratorUser = listUserIds.iterator();
                while(iteratorUser.hasNext()){
                    final Iterator<String> iteratorRoles = listRoles.iterator();
                    final String uid = iteratorUser.next();
                    while(iteratorRoles.hasNext()){
                        final HashMap<String,String> map = new HashMap<String,String>();
                        map.put("role_id",iteratorRoles.next());
                        map.put("user_id",uid);
                        listMaps.add(map);
                    }
                }
                final int rows = userDao.saveBatchUserRole(listUserIds,listMaps);
                return ToolClient.executeRows(rows);
            }
        }
        return ToolClient.jsonValidateField();
    }

    public String saveOwnMenu(final PageFormData pageFormData){
        final String p_userId = "userId";
        final String validate = ToolClient.validateField(pageFormData,p_userId);
        if(validate != null)return validate;
        final String kids = pageFormData.getString("kids");
        final String userId = pageFormData.getString(p_userId);
        final ArrayList<String> listMenus = ToolString.keysToList(kids);
        if(listMenus == null || listMenus.size() <= 0){
            final int rows = userDao.delOwnMenu(userId);
            return ToolClient.executeRows(rows,"操作成功","未做任何操作,因为暂无私有菜单");
        }else{
            final ArrayList<HashMap<String,String>> listMaps = new ArrayList<HashMap<String,String>>();
            final Iterator<String> iterator = listMenus.iterator();
            while(iterator.hasNext()){
                final HashMap<String,String> map = new HashMap<String,String>();
                map.put("menu_id",iterator.next());
                map.put("user_id",userId);
                listMaps.add(map);
            }
            final int rows = userDao.saveOwnMenu(userId,listMaps);
            return ToolClient.executeRows(rows);
        }
    }

    public String delByKeys(final PageFormData pageFormData){
        final String p_ids = "ids";
        final String validate = ToolClient.validateField(pageFormData,p_ids);
        if(validate != null)return validate;
        final String ids = pageFormData.getString(p_ids);
        final ArrayList<String> lists = ToolString.keysToList(ids);
        if(lists.contains(SUPER_KEY)){
            lists.remove(SUPER_KEY);
        }
        if(lists == null || lists.size() <= 0){
            return ToolClient.createJson(ConfigFile.code199,"请选择要删除的数据");
        }
        final int rows = userDao.delByKeys(lists);
        return ToolClient.executeRows(rows,"操作成功","用户已不存在,刷新重试");
    }

    public String editEnabled(final PageFormData pageFormData){
        final String p_disable = "disable";
        final String p_enabled = "enabled";
        final String p_userId = "userId";
        final String validate = ToolClient.validateField(pageFormData,p_disable,p_enabled,p_userId);
        if(validate != null)return validate;
        final String fieldInteger = ToolClient.validateInteger(pageFormData,p_disable,p_enabled);
        if(fieldInteger != null)return fieldInteger;
        final String kid = pageFormData.getString(p_userId);
        if(SUPER_KEY.equals(kid)){
            return ToolClient.createJson(ConfigFile.code204,"不能操作"+ConfigFile.KEY_SUPER+"账号");
        }
        final String user_name = userDao.queryExistById(kid);//查询是否存在
        if(user_name == null){
            return ToolClient.createJson(ConfigFile.code199,"账号已不存在");
        }
        final int rows = userDao.editEnabled(pageFormData);
        return ToolClient.executeRows(rows);
    }

    public boolean checkLogin(final String username,final String rawPassword){
        final String password = userDao.queryLogin(username);
        if(password == null)return false;
        return passworder.matches(rawPassword,password);
    }

    public String listData(PageFormData formData){
        final String filter = super.filterList(formData);
        if(filter != null) return filter;
        try {
            formData = ToolClient.dataTableMysql(formData);
            if(formData == null)return ToolClient.jsonValidateField();
            final String userId = LocalUserId.get();
            final String loginUser = userDao.queryExistById(userId);
            formData.put("userId",userId);
            if(ConfigFile.KEY_SUPER.equals(loginUser)){
                formData.put("keySuper",loginUser);
            }
            final HashMap<String,Object> map = userDao.listData(formData);
            return ToolClient.dataTableOK((List<Object>)map.get(ConfigFile.rows),map.get(ConfigFile.total),formData.get("sEcho"));
        } catch (Exception e){
            e.printStackTrace();
            return ToolClient.dataTableException(formData.get("sEcho"));
        }
    }

    //根据指定userid获取菜单用于分配私有菜单
    public String getOwnMenu(final PageFormData pageFormData){
        final String p_userId = "userId";
        final String validate = ToolClient.validateField(pageFormData,p_userId);
        if(validate != null)return validate;
        final String userId = pageFormData.getString(p_userId);
        final String loginId = LocalUserId.get();
        final String loginUser = userDao.queryExistById(loginId);
        if(ConfigFile.KEY_SUPER.equals(loginUser)){
            return ToolClient.queryJson(userDao.getOwnMenuForSuper(userId));
        }
        pageFormData.put("loginId",loginId);
        return ToolClient.queryJson(userDao.getOwnMenuForLogin(pageFormData));
    }

    //查看指定userid权限菜单数据
    public String getMenuData(final PageFormData pageFormData){
        final String p_userId = "userId";
        final String validate = ToolClient.validateField(pageFormData,p_userId);
        if(validate != null)return validate;
        final String userId = pageFormData.getString(p_userId);
        return ToolClient.queryJson(userDao.getMenuData(userId));
    }

    //仅保存userId,userName,roles 即可
    public HashMap<String,String> buildToken(final String userId){
        final HashMap<String,String> token = new HashMap<>(2);
        token.put(ConfigFile.REFRESH_TOKEN,ToolJWT.expireRefreshToken(userId));
        token.put(ConfigFile.ACCESS_TOKEN,ToolJWT.expireAccessToken(userId));
        return token;
    }

    public HashMap<String,String> refreshToken(final String userId){
        final HashMap<String,String> token = new HashMap<>(2);
        token.put(ConfigFile.REFRESH_TOKEN,ToolJWT.buildRefreshToken(userId));
        token.put(ConfigFile.ACCESS_TOKEN,ToolJWT.buildAccessToken(userId));
        return token;
    }

    /**自行修改密码*/
    public String editPassword(final PageFormData pageFormData){
        final String p_original_password = "original_password";
        final String p_new_password = "new_password";
        final String p_verify_password = "verify_password";
        final String validate = ToolClient.validateField(pageFormData,p_original_password,p_new_password,p_verify_password);
        if(validate != null)return validate;
        final String original_password = pageFormData.getString(p_original_password);
        final String password = pageFormData.getString(p_new_password);
        final String verify = pageFormData.getString(p_verify_password);
        if(!password.equals(verify)){
            return ToolClient.createJson(ConfigFile.code199,"输入的两次密码不一致");
        }
        final String userId = LocalUserId.get();
        final String pwd = userDao.getPassword(userId);
        final boolean bl = passworder.matches(original_password,pwd);
        if(!bl){
            return ToolClient.createJson(ConfigFile.code199,"你输入的原始密码不正确");
        }
        pageFormData.put("userId",userId);
        pageFormData.put("password",passworder.encode(password));
        final int rows = userDao.editPassword(pageFormData);
        return ToolClient.executeRows(rows);
    }

    /**修改个人信息*/
    public String editPersionInfo(final PageFormData formData){
        final String p_real_name = "real_name";
        final String p_affiliated_unit = "affiliated_unit";
        final String validate = ToolClient.validateField(formData,p_real_name,p_affiliated_unit);
        if(validate != null)return validate;
        final String userId = LocalUserId.get();
        formData.put("user_id",userId);
        final int rows = userDao.editPersionInfo(formData);
        return ToolClient.executeRows(rows);
    }

    /**获取位置地点名称*/
    public String getListSite(final String value){
        return ToolClient.queryJson(userDao.getListSite(value));
    }

    /**账号绑定到位置地点*/
    public String addBindSite(final PageFormData formData){
        final String p_userId = "userId";
        final String p_siteId = "siteId";
        final String validate = ToolClient.validateField(formData,p_userId);
        if(validate != null) return validate;
        final String siteId = formData.getString(p_siteId);
        if(siteId == null){
            return ToolClient.executeRows(userDao.delBindSite(formData.getString(p_userId)));
        }else{
            return ToolClient.executeRows(userDao.addBindSite(formData,formData.getString(p_userId)));
        }
    }
}