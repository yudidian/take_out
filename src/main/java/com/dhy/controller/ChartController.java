package com.dhy.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dhy.entity.Chart;
import com.dhy.entity.Employee;
import com.dhy.entity.User;
import com.dhy.service.ChartService;
import com.dhy.service.EmployeeService;
import com.dhy.service.UserService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Api(value = "聊天室控制类")
@ServerEndpoint("/websocket/chart")
@Controller
@Component
@Slf4j
public class ChartController {
    private static ApplicationContext applicationContext;
    //    存储聊天类容最大list长度
    private static final int MAX_SIZE = 3;
    /**
     * 设置在线人数为静态变量
     */
    public static int onlineNumber = 0;

    private ChartService chartService;

    private EmployeeService employeeService;

    private UserService userService;

    /**
     * 新建list集合存储数据
     */
    private static ArrayList<Chart> chartArrayList = new ArrayList<>();

    /**
     * map(username,websocket)作为对象添加到集合中
     */
    private static Map<String, ChartController> clients = new ConcurrentHashMap<String, ChartController>();

    /**
     * session会话
     */
    private Session session;

    private Chart chart;
    /**
     * 用户名称
     */
    private String userId;
    /**
     * 用户名称
     */
    private String managerId;

    public static synchronized int getOnlineCount() {
        return onlineNumber;
    }

    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        onlineNumber++;
        Chart chart = new Chart();
        Map<String, String> map = new HashMap<>();
        String queryString = session.getQueryString();
        String[] query = queryString.split("&");
        for (String s : query) {
            String[] keyAndValue = s.split("=");
            map.put(keyAndValue[0], keyAndValue[1]);
        }
        String userId = map.get("userId");
        String managerId = map.get("managerId");
        if (userId != null) {
            this.userId = userId;
            userService = applicationContext.getBean(UserService.class);
            User user = userService.getById(Long.valueOf(userId));
            List<Chart> list = getMineMessageList(userId);
            this.sendMessage(JSONObject.toJSONString(list));
            chart.setUserId(Long.valueOf(userId));
            chart.setUserName(user.getName());
            chart.setUserAvatar(user.getAvatar());
            chart.setSendFlag(1);
            this.chart = chart;
            clients.put(userId, this);
        } else {
            this.managerId = managerId;
            employeeService = applicationContext.getBean(EmployeeService.class);
            chartService = applicationContext.getBean(ChartService.class);
            Employee employee = employeeService.getById(Long.valueOf(managerId));
            Map<String, List<Chart>> userMessageList = getUserMessageList();
            this.sendMessage(JSONObject.toJSONString(userMessageList));
            chart.setUserId(Long.valueOf(managerId));
            chart.setManagerId(employee.getId());
            chart.setManagerName(employee.getName());
            chart.setManagerAvatar(employee.getAvatar());
            chart.setSendFlag(0);
            this.chart = chart;
            clients.put(managerId, this);
            LambdaQueryWrapper<Chart> chartLambdaQueryWrapper = new LambdaQueryWrapper<>();
            chartLambdaQueryWrapper.groupBy(Chart::getUserId).orderByDesc(Chart::getCreateTime);
        }
        log.info("现在来连接的客户id：{}====={}", map.get("userId"), map.get("managerId"));
        log.info("有新连接加入！ 当前在线人数 :{}", onlineNumber);
    }

    @OnClose
    public void onClose() {
        onlineNumber--;
        if (chart != null) {
            if (chart.getUserId() != null) {
                clients.remove(chart.getUserId().toString());
            } else {
                assert chart != null;
                clients.remove(chart.getManagerId().toString());
            }
        }
    }

    @OnMessage
    public void onMessage(String message) {
        JSONObject jsonObject = JSONObject.parseObject(message);
        String userId = jsonObject.getString("userId");
        String managerId = jsonObject.getString("managerId");
        if (userId != null && managerId == null) {
            ChartController chartController = clients.get(userId);
            ChartController managerServer = clients.get("1");
            Chart c = new Chart();
            BeanUtils.copyProperties(chartController.getChart(), c);
            c.setCreateTime(new Date());
            c.setMessage(jsonObject.getString("message"));
            c.setId(String.valueOf(UUID.randomUUID()));
            chartController.getChartArrayList().add(c);
            chartService = applicationContext.getBean(ChartService.class);
            chartController.getChartArrayList().forEach(item -> {
                chartService.save(item);
            });
            chartController.getChartArrayList().clear();
            List<Chart> messageList = getMineMessageList(userId);
            chartController.sendMessageTo(JSONObject.toJSONString(messageList), chartController.session);
            if (managerServer != null) {
                Map<String, List<Chart>> userMessageList = getUserMessageList();
                chartController.sendMessageTo(JSONObject.toJSONString(userMessageList), managerServer.session);
            }
        } else {
            ChartController userServer = clients.get(userId);
            ChartController chartController = clients.get(managerId);
            Chart c = new Chart();
            BeanUtils.copyProperties(chartController.getChart(), c);
            c.setMessage(jsonObject.getString("message"));
            c.setId(String.valueOf(UUID.randomUUID()));
            c.setCreateTime(new Date());
            assert userId != null;
            c.setUserId(Long.valueOf(userId));
            chartController.getChartArrayList().add(c);
            chartService = applicationContext.getBean(ChartService.class);
            chartController.getChartArrayList().forEach(item -> {
                chartService.save(item);
            });
            chartController.getChartArrayList().clear();
            Map<String, List<Chart>> userMessageList = getUserMessageList();
            chartController.sendMessageTo(JSONObject.toJSONString(userMessageList), chartController.session);
            if (userServer != null) {
                List<Chart> messageList = getMineMessageList(userId);
                userServer.sendMessageTo(JSONObject.toJSONString(messageList), userServer.session);
            }
        }
    }

    public void sendMessage(String message) {
        try {
            this.session.getBasicRemote().sendText(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendMessageTo(String message, Session session) {
        try {
            session.getBasicRemote().sendText(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setChartArrayList(Chart chart) {
        chartArrayList.add(chart);
    }

    public ArrayList<Chart> getChartArrayList() {
        return chartArrayList;
    }

    public Chart getChart() {
        return this.chart;
    }

    public static void setApplicationContext(ApplicationContext applicationContext) {
        ChartController.applicationContext = applicationContext;
    }

    public List<Chart> getMineMessageList(String userId) {
        chartService = applicationContext.getBean(ChartService.class);
        LambdaQueryWrapper<Chart> chartLambdaQueryWrapper = new LambdaQueryWrapper<>();
        chartLambdaQueryWrapper.eq(userId != null, Chart::getUserId, userId)
                .orderByAsc(Chart::getCreateTime)
                .last("limit 100");
        return chartService.list(chartLambdaQueryWrapper);
    }

    public Map<String, List<Chart>> getUserMessageList() {
        chartService = applicationContext.getBean(ChartService.class);
        LambdaQueryWrapper<Chart> chartLambdaQueryWrapper = new LambdaQueryWrapper<>();
        chartLambdaQueryWrapper
                .orderByAsc(Chart::getCreateTime)
                .groupBy(Chart::getUserId);
        List<Chart> list = chartService.list(chartLambdaQueryWrapper);
        HashMap<String, List<Chart>> hashMap = new HashMap<>();
        list.forEach(item -> {
            LambdaQueryWrapper<Chart> chartQuery = new LambdaQueryWrapper<>();
            chartQuery.eq(Chart::getUserId, item.getUserId()).orderByAsc(Chart::getCreateTime);
            List<Chart> chartList = chartService.list(chartQuery);
            hashMap.put(item.getUserId().toString(), chartList);
        });
        return hashMap;
    }
}
