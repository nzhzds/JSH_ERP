package com.jsh.action.basic;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.dao.DataAccessException;

import com.jsh.base.BaseAction;
import com.jsh.base.Log;
import com.jsh.model.po.Logdetails;
import com.jsh.model.po.Account;
import com.jsh.model.vo.basic.AccountModel;
import com.jsh.service.basic.AccountIService;
import com.jsh.util.common.PageUtil;
/**
 * 结算账户
 * @author ji sheng hua
 */
@SuppressWarnings("serial")
public class AccountAction extends BaseAction<AccountModel>
{
    private AccountIService accountService;
    private AccountModel model = new AccountModel();

    /**
     * 增加结算账户
     * @return
     */
    public void create()
    {
        Log.infoFileSync("==================开始调用增加结算账户方法===================");
        Boolean flag = false;
        try
        {
            Account Account = new Account();
            Account.setName(model.getName());
            Account.setSerialNo(model.getSerialNo());
            Account.setInitialAmount(model.getInitialAmount());
            Account.setCurrentAmount(model.getCurrentAmount());
            Account.setRemark(model.getRemark());
            accountService.create(Account);

            //========标识位===========
            flag = true;
            //记录操作日志使用
            tipMsg = "成功";
            tipType = 0;
        }
        catch (DataAccessException e)
        {
            Log.errorFileSync(">>>>>>>>>>>>>>>>>>>增加结算账户异常", e);
            flag = false;
            tipMsg = "失败";
            tipType = 1;
        }
        finally
        {
            try 
            {
                toClient(flag.toString());
            } 
            catch (IOException e) 
            {
                Log.errorFileSync(">>>>>>>>>>>>增加结算账户回写客户端结果异常", e);
            }
        }

        logService.create(new Logdetails(getUser(), "增加结算账户", model.getClientIp(),
                                         new Timestamp(System.currentTimeMillis())
                                         , tipType, "增加结算账户名称为  "+ model.getName() + " " + tipMsg + "！", "增加结算账户" + tipMsg));
        Log.infoFileSync("==================结束调用增加结算账户方法===================");
    }

    /**
     * 删除结算账户
     * @return
     */
    public String delete()
    {
        Log.infoFileSync("====================开始调用删除结算账户信息方法delete()================");
        try 
        {
            accountService.delete(model.getAccountID());
            tipMsg = "成功";
            tipType = 0;
        } 
        catch (DataAccessException e) 
        {
            Log.errorFileSync(">>>>>>>>>>>删除ID为 " + model.getAccountID() + "  的结算账户异常", e);
            tipMsg = "失败";
            tipType = 1;
        }
        model.getShowModel().setMsgTip(tipMsg);
        logService.create(new Logdetails(getUser(), "删除结算账户", model.getClientIp(),
                                         new Timestamp(System.currentTimeMillis())
                                         , tipType, "删除结算账户ID为  "+ model.getAccountID() + ",名称为  " + model.getName() + tipMsg + "！", "删除结算账户" + tipMsg));
        Log.infoFileSync("====================结束调用删除结算账户信息方法delete()================");
        return SUCCESS;
    }

    /**
     * 更新结算账户
     * @return
     */
    public void update()
    {
        Boolean flag = false;
        try
        {
            Account Account = accountService.get(model.getAccountID());
            Account.setName(model.getName());
            Account.setSerialNo(model.getSerialNo());
            Account.setInitialAmount(model.getInitialAmount());
            Account.setCurrentAmount(model.getCurrentAmount());
            Account.setRemark(model.getRemark());
            accountService.update(Account);

            flag = true;
            tipMsg = "成功";
            tipType = 0;
        } 
        catch (DataAccessException e) 
        {
            Log.errorFileSync(">>>>>>>>>>>>>修改结算账户ID为 ： " + model.getAccountID() + "信息失败", e);
            flag = false;
            tipMsg = "失败";
            tipType = 1;
        }
        finally
        {
            try 
            {
                toClient(flag.toString());
            } 
            catch (IOException e) 
            {
                Log.errorFileSync(">>>>>>>>>>>>修改结算账户回写客户端结果异常", e);
            }
        }
        logService.create(new Logdetails(getUser(), "更新结算账户", model.getClientIp(),
                                         new Timestamp(System.currentTimeMillis())
                                         , tipType, "更新结算账户ID为  "+ model.getAccountID() + " " + tipMsg + "！", "更新结算账户" + tipMsg));
    }

    /**
     * 批量删除指定ID结算账户
     * @return
     */
    public String batchDelete()
    {
        try
        {
            accountService.batchDelete(model.getAccountIDs());
            model.getShowModel().setMsgTip("成功");
            //记录操作日志使用
            tipMsg = "成功";
            tipType = 0;
        } 
        catch (DataAccessException e) 
        {
            Log.errorFileSync(">>>>>>>>>>>批量删除结算账户ID为：" + model.getAccountIDs() + "信息异常", e);
            tipMsg = "失败";
            tipType = 1;
        }

        logService.create(new Logdetails(getUser(), "批量删除结算账户", model.getClientIp(),
                                         new Timestamp(System.currentTimeMillis())
                                         , tipType, "批量删除结算账户ID为  "+ model.getAccountIDs() + " " + tipMsg + "！", "批量删除结算账户" + tipMsg));
        return SUCCESS;
    }

    /**
     * 检查输入名称是否存在
     */
    public void checkIsNameExist()
    {
        Boolean flag = false;
        try 
        {
            flag = accountService.checkIsNameExist("name", model.getName(), "id", model.getAccountID());
        } 
        catch (DataAccessException e) 
        {
            Log.errorFileSync(">>>>>>>>>>>>>>>>>检查结算账户名称为：" + model.getName() + " ID为： " + model.getAccountID() + " 是否存在异常！");
        }
        finally
        {
            try 
            {
                toClient(flag.toString());
            }
            catch (IOException e) 
            {
                Log.errorFileSync(">>>>>>>>>>>>回写检查结算账户名称为：" + model.getName() + " ID为： " + model.getAccountID() + " 是否存在异常！",e);
            }
        }
    }

    /**
     * 查找结算账户信息
     * @return
     */
    public void findBy()
    {
        try 
        {
            PageUtil<Account> pageUtil = new  PageUtil<Account>();
            pageUtil.setPageSize(model.getPageSize());
            pageUtil.setCurPage(model.getPageNo());
            pageUtil.setAdvSearch(getCondition());
            accountService.find(pageUtil);
            List<Account> dataList = pageUtil.getPageList();

            JSONObject outer = new JSONObject();
            outer.put("total", pageUtil.getTotalCount());
            //存放数据json数组
            JSONArray dataArray = new JSONArray();
            if(null != dataList)
            {
                for(Account account:dataList)
                {
                    JSONObject item = new JSONObject();
                    item.put("id", account.getId());
                    //结算账户名称
                    item.put("name", account.getName());
                    item.put("serialNo", account.getSerialNo());
                    item.put("initialAmount", account.getInitialAmount());
                    item.put("currentAmount", account.getCurrentAmount());
                    item.put("remark", account.getRemark());
                    item.put("op", 1);
                    dataArray.add(item);
                }
            }
            outer.put("rows", dataArray);
            //回写查询结果
            toClient(outer.toString());
        } 
        catch (DataAccessException e) 
        {
            Log.errorFileSync(">>>>>>>>>查找结算账户信息异常", e);
        } 
        catch (IOException e) 
        {
            Log.errorFileSync(">>>>>>>>>回写查询结算账户信息结果异常", e);
        }
    }

    /**
     * 查找结算账户信息-下拉框
     * @return
     */
    public void findBySelect()
    {
        try 
        {
            PageUtil<Account> pageUtil = new  PageUtil<Account>();
            pageUtil.setPageSize(0);
            pageUtil.setCurPage(0);
            pageUtil.setAdvSearch(getCondition_select());
            accountService.find(pageUtil);
            List<Account> dataList = pageUtil.getPageList();
            //存放数据json数组
            JSONArray dataArray = new JSONArray();
            if(null != dataList)
            {
                for(Account account:dataList)
                {
                    JSONObject item = new JSONObject();
                    item.put("id", account.getId());
                    //结算账户名称
                    item.put("name", account.getName());
                    dataArray.add(item);
                }
            }
            //回写查询结果
            toClient(dataArray.toString());
        } 
        catch (DataAccessException e) 
        {
            Log.errorFileSync(">>>>>>>>>查找结算账户信息异常", e);
        } 
        catch (IOException e) 
        {
            Log.errorFileSync(">>>>>>>>>回写查询结算账户信息结果异常", e);
        }
    }

    /**
     * 拼接搜索条件
     * @return
     */
    private Map<String,Object> getCondition()
    {
        /**
         * 拼接搜索条件
         */
        Map<String,Object> condition = new HashMap<String,Object>();
        condition.put("name_s_like", model.getName());
        condition.put("serialNo_s_like", model.getSerialNo());
        condition.put("remark_s_like", model.getRemark());
        condition.put("id_s_order", "desc");
        return condition;
    }

    /**
     * 拼接搜索条件-下拉框-结算账户
     * @return
     */
    private Map<String,Object> getCondition_select()
    {
        /**
         * 拼接搜索条件
         */
        Map<String,Object> condition = new HashMap<String,Object>();
        condition.put("id_s_order", "desc");
        return condition;
    }

    //=============以下spring注入以及Model驱动公共方法，与Action处理无关==================
    @Override
    public AccountModel getModel()
    {
        return model;
    }
    public void setAccountService(AccountIService accountService)
    {
        this.accountService = accountService;
    }
}
