package com.augmentum.ufsch.admin.controller;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.jxls.common.Context;
import org.jxls.util.JxlsHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.augmentum.ufsch.admin.exception.NotFoundException;
import com.augmentum.ufsch.admin.interceptor.Layout;
import com.augmentum.ufsch.admin.service.IAgentService;
import com.augmentum.ufsch.admin.service.ICustomerService;
import com.augmentum.ufsch.admin.service.IMessageService;
import com.augmentum.ufsch.admin.service.ISessionService;
import com.augmentum.ufsch.admin.util.DateTimeUtil;
import com.augmentum.ufsch.admin.vo.AgentWorkVo;
import com.augmentum.ufsch.admin.vo.ConsultingVo;
import com.augmentum.ufsch.admin.vo.OverallInlineVo;
import com.augmentum.ufsch.admin.vo.ResponseTimeVo;
import com.augmentum.ufsch.admin.vo.SatisfiedSessionVo;
import com.augmentum.ufsch.admin.vo.SessionTimeVo;
import com.augmentum.ufsch.admin.vo.response.DissatisfiedData;
import com.augmentum.ufsch.admin.vo.response.RespData;
import com.augmentum.ufsch.common.model.Agent;
import com.augmentum.ufsch.common.utils.SurveyConstant;

@Layout("layout/main")
@Controller
@RequestMapping("/report")
public class ReportController {

    @Autowired
    private ISessionService sessionService;

    @Autowired
    private IAgentService agentService;

    @Autowired
    private IMessageService messageService;

    @Autowired
    private ICustomerService customerService;

    @Autowired
    private ApplicationContext appContext;

    public void setSessionService(ISessionService sessionService) {
        this.sessionService = sessionService;
    }

    @RequestMapping(value = "/response/time", method = RequestMethod.GET)
    public ModelAndView responseTimeReport(String[] agentIds, String fromDate, String toDate) {
        if (agentIds == null) {
            agentIds = new String[0];
        }

        Date fromDateTime = DateTimeUtil.getDateFromDateTimeString(fromDate);
        Date toDateTime = DateTimeUtil.getDateFromDateTimeString(toDate);

        if (fromDateTime != null) {
            Date toDateTimeMax = DateTimeUtil.getNewDateWithIntervalDays(fromDateTime, 29);
            if (toDateTime == null || (toDateTime != null && toDateTimeMax.getTime() < toDateTime.getTime())) {
                toDateTime = toDateTimeMax;
            }
        }
        if (toDateTime != null && fromDateTime == null) {
            fromDateTime = DateTimeUtil.getNewDateWithIntervalDays(toDateTime, -29);
        }

        List<Agent> agents = agentService.findAll();

        List<String> agentIdsData = Arrays.asList(agentIds);
        List<Agent> selectedAgents = new ArrayList<Agent>();
        if (!agentIdsData.isEmpty()) {
            for (Agent agent : agents) {
                if (agentIdsData.contains(agent.getId())) {
                    selectedAgents.add(agent);
                }
            }
        } else {
            selectedAgents = agents;
        }

        Map<String, List<ResponseTimeVo>> responses = new TreeMap<String, List<ResponseTimeVo>>();

        ModelAndView view = new ModelAndView();
        view.setViewName("report/response-time");
        view.addObject("agentIds", agentIds);
        view.addObject("agents", agents);
        view.addObject("fromDate", fromDateTime);
        view.addObject("toDate", toDateTime);
        view.addObject("active", "ufs-report");
        view.addObject("activeTable", "ufs-responseTime");
        view.addObject("responses", responses);
        view.addObject("selectedAgents", selectedAgents);

        return view;
    }

    @RequestMapping(value = "/response/time/data", method = RequestMethod.GET)
    @ResponseBody
    public RespData getResponseTimeReportData(String[] agentIds, String fromDate, String toDate) {
        if (agentIds == null) {
            agentIds = new String[0];
        }

        Date fromDateTime = DateTimeUtil.getDateFromDateTimeString(fromDate);
        Date toDateTime = DateTimeUtil.getDateFromDateTimeString(toDate);

        if (fromDateTime != null) {
            Date toDateTimeMax = DateTimeUtil.getNewDateWithIntervalDays(fromDateTime, 29);
            if (toDateTime == null || (toDateTime != null && toDateTimeMax.getTime() < toDateTime.getTime())) {
                toDateTime = toDateTimeMax;
            }
        }
        if (toDateTime != null && fromDateTime == null) {
            fromDateTime = DateTimeUtil.getNewDateWithIntervalDays(toDateTime, -29);
        }

        List<String> agentIdsData = Arrays.asList(agentIds);
        Map<String, List<ResponseTimeVo>> agentWorks = sessionService.findResponseTimeReport(agentIdsData, fromDateTime,
                toDateTime);

        RespData resp = new RespData();
        resp.setData(agentWorks.values());
        return resp;
    }

    @RequestMapping(value = "/response/time/export", method = RequestMethod.GET)
    public void exportResponseTimeReportData(String[] agentIds, String fromDate, String toDate,
            HttpServletResponse response) throws Exception {
        if (agentIds == null) {
            agentIds = new String[0];
        }

        Date fromDateTime = DateTimeUtil.getDateFromDateTimeString(fromDate);
        Date toDateTime = DateTimeUtil.getDateFromDateTimeString(toDate);

        if (fromDateTime != null) {
            Date toDateTimeMax = DateTimeUtil.getNewDateWithIntervalDays(fromDateTime, 29);
            if (toDateTime == null || (toDateTime != null && toDateTimeMax.getTime() < toDateTime.getTime())) {
                toDateTime = toDateTimeMax;
            }
        }
        if (toDateTime != null && fromDateTime == null) {
            fromDateTime = DateTimeUtil.getNewDateWithIntervalDays(toDateTime, -29);
        }

        List<String> agentIdsData = Arrays.asList(agentIds);
        Map<String, List<ResponseTimeVo>> ResponseTimes = sessionService.findResponseTimeReport(agentIdsData,
                fromDateTime, toDateTime);

        List<String> agents = new ArrayList<>();
        for (List<ResponseTimeVo> firstRow : ResponseTimes.values()) {
            for (ResponseTimeVo ResponseTimeItem : firstRow) {
                agents.add(ResponseTimeItem.getName());
            }
            break;
        }

        Resource resource = appContext.getResource("classpath:report/response-time.xlsx");
        if (resource != null && resource.exists()) {
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition",
                    "attachment;filename=" + URLEncoder.encode("响应时间报表.xlsx", "UTF-8"));
            Context xlsContext = new Context();
            xlsContext.putVar("dates", ResponseTimes.keySet());
            xlsContext.putVar("rows", ResponseTimes);
            xlsContext.putVar("agents", agents);
            try {
                JxlsHelper.getInstance().processTemplate(resource.getInputStream(), response.getOutputStream(),
                        xlsContext);
            } catch (IOException e) {
                throw e;
            }
        } else {
            throw new NotFoundException();
        }
    }

    @RequestMapping(value = "/session/time", method = RequestMethod.GET)
    public ModelAndView getSessionTimeReport(String[] agentIds, String fromDate, String toDate) {
        if (agentIds == null) {
            agentIds = new String[0];
        }

        Date fromDateTime = DateTimeUtil.getDateFromDateTimeString(fromDate);
        Date toDateTime = DateTimeUtil.getDateFromDateTimeString(toDate);

        if (fromDateTime != null) {
            Date toDateTimeMax = DateTimeUtil.getNewDateWithIntervalDays(fromDateTime, 29);
            if (toDateTime == null || (toDateTime != null && toDateTimeMax.getTime() < toDateTime.getTime())) {
                toDateTime = toDateTimeMax;
            }
        }
        if (toDateTime != null && fromDateTime == null) {
            fromDateTime = DateTimeUtil.getNewDateWithIntervalDays(toDateTime, -29);
        }

        List<Agent> agents = agentService.findAll();
        List<String> agentIdsData = Arrays.asList(agentIds);
        List<Agent> selectedAgents = new ArrayList<Agent>();
        if (!agentIdsData.isEmpty()) {
            for (Agent agent : agents) {
                if (agentIdsData.contains(agent.getId())) {
                    selectedAgents.add(agent);
                }
            }
        } else {
            selectedAgents = agents;
        }

        Map<String, List<SessionTimeVo>> sessionTimeData = new TreeMap<String, List<SessionTimeVo>>();

        ModelAndView view = new ModelAndView();
        view.setViewName("report/session-time");
        view.addObject("sessionTimeData", sessionTimeData);
        view.addObject("agentIds", agentIds);
        view.addObject("agents", agents);
        view.addObject("selectedAgents", selectedAgents);
        view.addObject("fromDate", fromDateTime);
        view.addObject("toDate", toDateTime);
        view.addObject("active", "ufs-report");
        view.addObject("activeTable", "ufs-sessionTime");

        return view;
    }

    @RequestMapping(value = "/session/time/data", method = RequestMethod.GET)
    @ResponseBody
    public RespData getSessionTimeReportData(String[] agentIds, String fromDate, String toDate) {
        RespData resp = new RespData();

        if (agentIds == null) {
            agentIds = new String[0];
        }

        Date fromDateTime = DateTimeUtil.getDateFromDateTimeString(fromDate);
        Date toDateTime = DateTimeUtil.getDateFromDateTimeString(toDate);

        if (fromDateTime != null) {
            Date toDateTimeMax = DateTimeUtil.getNewDateWithIntervalDays(fromDateTime, 29);
            if (toDateTime == null || (toDateTime != null && toDateTimeMax.getTime() < toDateTime.getTime())) {
                toDateTime = toDateTimeMax;
            }
        }
        if (toDateTime != null && fromDateTime == null) {
            fromDateTime = DateTimeUtil.getNewDateWithIntervalDays(toDateTime, -29);
        }

        List<String> agentIdsData = Arrays.asList(agentIds);
        Map<String, List<SessionTimeVo>> sessionTimeData = sessionService.findSessionTimeReport(agentIdsData,
                fromDateTime, toDateTime);
        resp.setData(sessionTimeData.values());

        return resp;
    }

    @RequestMapping(value = "/session/time/export", method = RequestMethod.GET)
    public void exportSessionTimeReport(String[] agentIds, String fromDate, String toDate, HttpServletResponse response)
            throws Exception {
        if (agentIds == null) {
            agentIds = new String[0];
        }

        Date fromDateTime = DateTimeUtil.getDateFromDateTimeString(fromDate);
        Date toDateTime = DateTimeUtil.getDateFromDateTimeString(toDate);

        if (fromDateTime != null) {
            Date toDateTimeMax = DateTimeUtil.getNewDateWithIntervalDays(fromDateTime, 29);
            if (toDateTime == null || (toDateTime != null && toDateTimeMax.getTime() < toDateTime.getTime())) {
                toDateTime = toDateTimeMax;
            }
        }
        if (toDateTime != null && fromDateTime == null) {
            fromDateTime = DateTimeUtil.getNewDateWithIntervalDays(toDateTime, -29);
        }

        List<String> agentIdsData = Arrays.asList(agentIds);
        Map<String, List<SessionTimeVo>> sessionTimeData = sessionService.findSessionTimeReport(agentIdsData,
                fromDateTime, toDateTime);
        List<String> agents = new ArrayList<>();
        for (List<SessionTimeVo> firstRow : sessionTimeData.values()) {
            for (SessionTimeVo item : firstRow) {
                agents.add(item.getName());
            }
            break;
        }

        Resource resource = appContext.getResource("classpath:report/session-time.xlsx");
        if (resource != null && resource.exists()) {
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition",
                    "attachment;filename=" + URLEncoder.encode("会话时间报表.xlsx", "UTF-8"));
            Context xlsContext = new Context();
            xlsContext.putVar("dates", sessionTimeData.keySet());
            xlsContext.putVar("rows", sessionTimeData);
            xlsContext.putVar("agents", agents);
            try {
                JxlsHelper.getInstance().processTemplate(resource.getInputStream(), response.getOutputStream(),
                        xlsContext);
            } catch (IOException e) {
                throw e;
            }
        } else {
            throw new NotFoundException();
        }
    }

    @RequestMapping(value = "/dissatisfied", method = RequestMethod.GET)
    public ModelAndView dissatisfiedDetailReport(String[] agentIds, String fromDate, String toDate) {
        if (agentIds == null) {
            agentIds = new String[0];
        }

        Date fromDateTime = DateTimeUtil.getDateFromDateTimeString(fromDate);
        Date toDateTime = DateTimeUtil.getDateFromDateTimeString(toDate);

        if (fromDateTime != null) {
            Date toDateTimeMax = DateTimeUtil.getNewDateWithIntervalDays(fromDateTime, 29);
            if (toDateTime == null || (toDateTime != null && toDateTimeMax.getTime() < toDateTime.getTime())) {
                toDateTime = toDateTimeMax;
            }
        }
        if (toDateTime != null && fromDateTime == null) {
            fromDateTime = DateTimeUtil.getNewDateWithIntervalDays(toDateTime, -29);
        }

        List<String> agentIdsData = Arrays.asList(agentIds);
        List<DissatisfiedData> dissatisfiedDatas = sessionService.findDissatisfiedDetailReport(agentIdsData,
                fromDateTime, toDateTime, false);
        List<Agent> agents = agentService.findAll();

        ModelAndView view = new ModelAndView();
        view.setViewName("report/dissatisfied-detail");
        view.addObject("dissatisfiedDatas", dissatisfiedDatas);
        view.addObject("agentIds", agentIds);
        view.addObject("agents", agents);
        view.addObject("fromDate", fromDateTime);
        view.addObject("toDate", toDateTime);
        view.addObject("active", "ufs-report");
        view.addObject("activeTable", "ufs-dissatisfied");

        return view;
    }

    @RequestMapping(value = "/satisfied", method = RequestMethod.GET)
    public ModelAndView satisfiedDetailReport(String searchType, String fromDate, String toDate) {
        String searchTypeTitle = "日期";
        if (StringUtils.isBlank(searchType) || (SurveyConstant.SEARCH_REPORT_BY_DATE.equals(searchType)
                && SurveyConstant.SEARCH_REPORT_BY_AGENT.equals(searchType))) {
            searchType = SurveyConstant.SEARCH_REPORT_BY_DATE;
        } else if (SurveyConstant.SEARCH_REPORT_BY_AGENT.equals(searchType)) {
            searchTypeTitle = "客服";
        }

        Date fromDateTime = DateTimeUtil.getDateFromDateTimeString(fromDate);
        Date toDateTime = DateTimeUtil.getDateFromDateTimeString(toDate);

        if (fromDateTime != null) {
            Date toDateTimeMax = DateTimeUtil.getNewDateWithIntervalDays(fromDateTime, 29);
            if (toDateTime == null || (toDateTime != null && toDateTimeMax.getTime() < toDateTime.getTime())) {
                toDateTime = toDateTimeMax;
            }
        }
        if (toDateTime != null && fromDateTime == null) {
            fromDateTime = DateTimeUtil.getNewDateWithIntervalDays(toDateTime, -29);
        }

//      List<SatisfiedSessionVo> satisfiedSessionVos = sessionService.findSatisfiedDetailReport(searchType,
//              fromDateTime, toDateTime);
//
//      if (SurveyConstant.SEARCH_REPORT_BY_AGENT.equals(searchType)) {
//          List<Agent> agents = agentService.findAll();
//          Map<String, Agent> agentMap = new TreeMap<String, Agent>();
//          for (Agent agent : agents) {
//              agentMap.put(agent.getId(), agent);
//          }
//
//          for (SatisfiedSessionVo item : satisfiedSessionVos) {
//              String key = item.getKey();
//              if (agents.contains(key)) {
//                  Agent agent = agentMap.get(key);
//                  item.setKey(agent.getName());
//              }
//          }
//      }
        Map<String, List<SatisfiedSessionVo>> satisfiedSessionVos = new TreeMap<String, List<SatisfiedSessionVo>>();

        ModelAndView view = new ModelAndView();
        view.setViewName("report/satisfied-detail");
        view.addObject("satisfiedDatas", satisfiedSessionVos);
        view.addObject("searchType", searchType);
        view.addObject("searchTypeTitle", searchTypeTitle);
        view.addObject("fromDate", fromDateTime);
        view.addObject("toDate", toDateTime);
        view.addObject("active", "ufs-report");
        view.addObject("activeTable", "ufs-satisfied");

        return view;
    }

    @RequestMapping(value = "/satisfied/data", method = RequestMethod.GET)
    @ResponseBody
    public RespData getsatisfiedDetailReportData(String searchType, String fromDate, String toDate) {
        RespData resp = new RespData();

        Date fromDateTime = DateTimeUtil.getDateFromDateTimeString(fromDate);
        Date toDateTime = DateTimeUtil.getDateFromDateTimeString(toDate);

        if (fromDateTime != null) {
            Date toDateTimeMax = DateTimeUtil.getNewDateWithIntervalDays(fromDateTime, 29);
            if (toDateTime == null || (toDateTime != null && toDateTimeMax.getTime() < toDateTime.getTime())) {
                toDateTime = toDateTimeMax;
            }
        }
        if (toDateTime != null && fromDateTime == null) {
            fromDateTime = DateTimeUtil.getNewDateWithIntervalDays(toDateTime, -29);
        }

        List<SatisfiedSessionVo> satisfiedSessionVos = sessionService.findSatisfiedDetailReport(searchType,
                fromDateTime, toDateTime);
        if (SurveyConstant.SEARCH_REPORT_BY_AGENT.equals(searchType)) {
            List<Agent> agents = agentService.findAll();
            Map<String, Agent> agentMap = new TreeMap<String, Agent>();
            for (Agent agent : agents) {
                agentMap.put(agent.getId(), agent);
            }

            for (SatisfiedSessionVo item : satisfiedSessionVos) {
                String key = item.getKey();
                if (agents.contains(key)) {
                    Agent agent = agentMap.get(key);
                    item.setKey(agent.getName());
                }
            }
        }
        resp.setData(satisfiedSessionVos);

        return resp;
    }

    @RequestMapping(value = "/satisfied/export", method = RequestMethod.GET)
    @ResponseBody
    public void exportSatisfiedDetailReport(String searchType, String fromDate, String toDate,
            HttpServletResponse response) throws Exception {
        String searchTypeTitle = "日期";
        if (StringUtils.isBlank(searchType) || (SurveyConstant.SEARCH_REPORT_BY_DATE.equals(searchType)
                && SurveyConstant.SEARCH_REPORT_BY_AGENT.equals(searchType))) {
            searchType = SurveyConstant.SEARCH_REPORT_BY_DATE;
        } else if (SurveyConstant.SEARCH_REPORT_BY_AGENT.equals(searchType)) {
            searchTypeTitle = "客服";
        }

        Date fromDateTime = DateTimeUtil.getDateFromDateTimeString(fromDate);
        Date toDateTime = DateTimeUtil.getDateFromDateTimeString(toDate);

        if (fromDateTime != null) {
            Date toDateTimeMax = DateTimeUtil.getNewDateWithIntervalDays(fromDateTime, 29);
            if (toDateTime == null || (toDateTime != null && toDateTimeMax.getTime() < toDateTime.getTime())) {
                toDateTime = toDateTimeMax;
            }
        }
        if (toDateTime != null && fromDateTime == null) {
            fromDateTime = DateTimeUtil.getNewDateWithIntervalDays(toDateTime, -29);
        }

        List<SatisfiedSessionVo> satisfiedSessionVos = sessionService.findSatisfiedDetailReport(searchType,
                fromDateTime, toDateTime);
        if (SurveyConstant.SEARCH_REPORT_BY_AGENT.equals(searchType)) {
            List<Agent> agents = agentService.findAll();
            Map<String, Agent> agentMap = new TreeMap<String, Agent>();
            for (Agent agent : agents) {
                agentMap.put(agent.getId(), agent);
            }

            for (SatisfiedSessionVo item : satisfiedSessionVos) {
                String key = item.getKey();
                if (agents.contains(key)) {
                    Agent agent = agentMap.get(key);
                    item.setKey(agent.getName());
                }
            }
        }

        Resource resource = appContext.getResource("classpath:report/satisfied-" + searchType + ".xlsx");
        if (resource != null && resource.exists()) {
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment;filename="
                    + java.net.URLEncoder.encode("满意度报表（按" + searchTypeTitle + "）.xlsx", "UTF-8"));
            Context xlsContext = new Context();
            xlsContext.putVar("rows", satisfiedSessionVos);
            try {
                JxlsHelper.getInstance().processTemplate(resource.getInputStream(), response.getOutputStream(),
                        xlsContext);
            } catch (IOException e) {
                throw e;
            }
        } else {
            throw new NotFoundException();
        }
    }

    @RequestMapping("/{sessionId}/allmessages")
    @ResponseBody
    public RespData getAllmessages(@PathVariable("sessionId") String sessionId) {
        return messageService.findAllMessagesWithAvatarsBySessionId(sessionId);
    }

    @RequestMapping(value = "/consulting/time", method = RequestMethod.GET)
    public ModelAndView getConsultingReport(String way, String fromDate, String toDate) {
        List<ConsultingVo> consultings = null;

        Date fromDateTime = DateTimeUtil.getDateFromDateTimeString(fromDate);
        Date toDateTime = DateTimeUtil.getDateFromDateTimeString(toDate);

        if (fromDateTime != null) {
            Date toDateTimeMax = DateTimeUtil.getNewDateWithIntervalDays(fromDateTime, 29);
            if (toDateTime == null || (toDateTime != null && toDateTimeMax.getTime() < toDateTime.getTime())) {
                toDateTime = toDateTimeMax;
            }
        }
        if (toDateTime != null && fromDateTime == null) {
            fromDateTime = DateTimeUtil.getNewDateWithIntervalDays(toDateTime, -29);
        }

        ModelAndView view = new ModelAndView();
        view.setViewName("report/consulting-time");
        view.addObject("way", way);
        view.addObject("fromDate", fromDateTime);
        view.addObject("toDate", toDateTime);
        view.addObject("consultings", consultings);
        view.addObject("active", "ufs-report");
        view.addObject("activeTable", "ufs-consultingTime");

        return view;
    }

    @RequestMapping(value = "/consulting/time/data", method = RequestMethod.GET)
    @ResponseBody
    public RespData getConsultingTimeReportData(String way, String fromDate, String toDate) {
        RespData resp = new RespData();
        List<ConsultingVo> consultings = null;

        Date fromDateTime = DateTimeUtil.getDateFromDateTimeString(fromDate);
        Date toDateTime = DateTimeUtil.getDateFromDateTimeString(toDate);

        if (fromDateTime != null) {
            Date toDateTimeMax = DateTimeUtil.getNewDateWithIntervalDays(fromDateTime, 29);
            if (toDateTime == null || (toDateTime != null && toDateTimeMax.getTime() < toDateTime.getTime())) {
                toDateTime = toDateTimeMax;
            }
        }
        if (toDateTime != null && fromDateTime == null) {
            fromDateTime = DateTimeUtil.getNewDateWithIntervalDays(toDateTime, -29);
        }

        if ("week".equals(way)) {
            consultings = sessionService.findConsultingWeekReport(fromDateTime, toDateTime);
        } else {
            consultings = sessionService.findConsultingHourReport(fromDateTime, toDateTime);
        }
        resp.setData(consultings);

        return resp;
    }

    @RequestMapping(value = "/consulting/time/export", method = RequestMethod.GET)
    @ResponseBody
    public void exportConsultingTimeReportData(String way, String fromDate, String toDate, HttpServletResponse response)
            throws Exception {
        Date fromDateTime = DateTimeUtil.getDateFromDateTimeString(fromDate);
        Date toDateTime = DateTimeUtil.getDateFromDateTimeString(toDate);

        if (fromDateTime != null) {
            Date toDateTimeMax = DateTimeUtil.getNewDateWithIntervalDays(fromDateTime, 29);
            if (toDateTime == null || (toDateTime != null && toDateTimeMax.getTime() < toDateTime.getTime())) {
                toDateTime = toDateTimeMax;
            }
        }
        if (toDateTime != null && fromDateTime == null) {
            fromDateTime = DateTimeUtil.getNewDateWithIntervalDays(toDateTime, -29);
        }

        List<ConsultingVo> consultings = null;
        Resource resource = null;
        String statisticalWay = null;

        if ("week".equals(way)) {
            String[] week = { "周一", "周二", "周三", "周四", "周五", "周六", "周日" };
            consultings = sessionService.findConsultingWeekReport(fromDateTime, toDateTime);

            // format export time
            for (ConsultingVo consulting : consultings) {
                consulting.setExportTime(week[consulting.getTime() - 1]);
            }

            resource = appContext.getResource("classpath:report/consulting-time-week.xlsx");
            statisticalWay = "（周）";
        } else {
            consultings = sessionService.findConsultingHourReport(fromDateTime, toDateTime);

            // format export time
            for (ConsultingVo consulting : consultings) {
                Integer time = consulting.getTime() - 1;
                consulting.setExportTime(time + ":00-" + time + ":59");
            }

            resource = appContext.getResource("classpath:report/consulting-time-hour.xlsx");
            statisticalWay = "（小时）";
        }

        if (resource != null && resource.exists()) {
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition",
                    "attachment;filename=" + URLEncoder.encode("咨询时间分布报表" + statisticalWay + ".xlsx", "UTF-8"));
            Context xlsContext = new Context();
            xlsContext.putVar("rows", consultings);
            try {
                JxlsHelper.getInstance().processTemplate(resource.getInputStream(), response.getOutputStream(),
                        xlsContext);
            } catch (IOException e) {
                throw e;
            }
        } else {
            throw new NotFoundException();
        }
    }

    @RequestMapping(value = "/agent/work", method = RequestMethod.GET)
    public ModelAndView getAgentWorkReport(String[] agentIds, String fromDate, String toDate) {
        if (agentIds == null) {
            agentIds = new String[0];
        }

        Date fromDateTime = DateTimeUtil.getDateFromDateTimeString(fromDate);
        Date toDateTime = DateTimeUtil.getDateFromDateTimeString(toDate);

        if (fromDateTime != null) {
            Date toDateTimeMax = DateTimeUtil.getNewDateWithIntervalDays(fromDateTime, 29);
            if (toDateTime == null || (toDateTime != null && toDateTimeMax.getTime() < toDateTime.getTime())) {
                toDateTime = toDateTimeMax;
            }
        }
        if (toDateTime != null && fromDateTime == null) {
            fromDateTime = DateTimeUtil.getNewDateWithIntervalDays(toDateTime, -29);
        }

        List<Agent> agents = agentService.findAll();

        List<String> agentIdsData = Arrays.asList(agentIds);
        List<Agent> selectedAgents = new ArrayList<Agent>();
        if (!agentIdsData.isEmpty()) {
            for (Agent agent : agents) {
                if (agentIdsData.contains(agent.getId())) {
                    selectedAgents.add(agent);
                }
            }
        } else {
            selectedAgents = agents;
        }

        Map<String, List<AgentWorkVo>> agentWorks = new TreeMap<String, List<AgentWorkVo>>();

        ModelAndView view = new ModelAndView();
        view.setViewName("report/agent-work");
        view.addObject("agentWorks", agentWorks);
        view.addObject("agentIds", agentIds);
        view.addObject("agents", agents);
        view.addObject("selectedAgents", selectedAgents);
        view.addObject("fromDate", fromDateTime);
        view.addObject("toDate", toDateTime);
        view.addObject("active", "ufs-report");
        view.addObject("activeTable", "ufs-agentWork");

        return view;
    }

    @RequestMapping(value = "/agent/work/data", method = RequestMethod.GET)
    @ResponseBody
    public RespData getAgentWorkReportData(String[] agentIds, String fromDate, String toDate) {
        if (agentIds == null) {
            agentIds = new String[0];
        }

        Date fromDateTime = DateTimeUtil.getDateFromDateTimeString(fromDate);
        Date toDateTime = DateTimeUtil.getDateFromDateTimeString(toDate);

        if (fromDateTime != null) {
            Date toDateTimeMax = DateTimeUtil.getNewDateWithIntervalDays(fromDateTime, 29);
            if (toDateTime == null || (toDateTime != null && toDateTimeMax.getTime() < toDateTime.getTime())) {
                toDateTime = toDateTimeMax;
            }
        }
        if (toDateTime != null && fromDateTime == null) {
            fromDateTime = DateTimeUtil.getNewDateWithIntervalDays(toDateTime, -29);
        }

        List<String> agentIdsData = Arrays.asList(agentIds);
        Map<String, List<AgentWorkVo>> agentWorks = sessionService.findAgentWorkReport(agentIdsData, fromDateTime,
                toDateTime);

        RespData resp = new RespData();
        resp.setData(agentWorks.values());
        return resp;
    }

    @RequestMapping(value = "/agent/work/export", method = RequestMethod.GET)
    public void exportAgentWorkReportData(String[] agentIds, String fromDate, String toDate,
            HttpServletResponse response) throws Exception {
        if (agentIds == null) {
            agentIds = new String[0];
        }

        Date fromDateTime = DateTimeUtil.getDateFromDateTimeString(fromDate);
        Date toDateTime = DateTimeUtil.getDateFromDateTimeString(toDate);

        if (fromDateTime != null) {
            Date toDateTimeMax = DateTimeUtil.getNewDateWithIntervalDays(fromDateTime, 29);
            if (toDateTime == null || (toDateTime != null && toDateTimeMax.getTime() < toDateTime.getTime())) {
                toDateTime = toDateTimeMax;
            }
        }
        if (toDateTime != null && fromDateTime == null) {
            fromDateTime = DateTimeUtil.getNewDateWithIntervalDays(toDateTime, -29);
        }

        List<String> agentIdsData = Arrays.asList(agentIds);
        Map<String, List<AgentWorkVo>> agentWorks = sessionService.findAgentWorkReport(agentIdsData, fromDateTime,
                toDateTime);

        List<String> agents = new ArrayList<>();
        for (List<AgentWorkVo> firstRow : agentWorks.values()) {
            for (AgentWorkVo workItem : firstRow) {
                agents.add(workItem.getName());
            }
            break;
        }

        Resource resource = appContext.getResource("classpath:report/agent-work.xlsx");
        if (resource != null && resource.exists()) {
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition",
                    "attachment;filename=" + URLEncoder.encode("客服工作量报表.xlsx", "UTF-8"));
            Context xlsContext = new Context();
            xlsContext.putVar("dates", agentWorks.keySet());
            xlsContext.putVar("rows", agentWorks);
            xlsContext.putVar("agents", agents);
            try {
                JxlsHelper.getInstance().processTemplate(resource.getInputStream(), response.getOutputStream(),
                        xlsContext);
            } catch (IOException e) {
                throw e;
            }
        } else {
            throw new NotFoundException();
        }
    }

    @RequestMapping(value = "/overall/inline", method = RequestMethod.GET)
    public ModelAndView overallInlineReport(String fromDate, String toDate) {
        List<OverallInlineVo> overallInlines = new ArrayList<OverallInlineVo>();

        Date fromDateTime = DateTimeUtil.getDateFromDateTimeString(fromDate);
        Date toDateTime = DateTimeUtil.getDateFromDateTimeString(toDate);

        if (fromDateTime != null) {
            Date toDateTimeMax = DateTimeUtil.getNewDateWithIntervalDays(fromDateTime, 29);
            if (toDateTime == null || (toDateTime != null && toDateTimeMax.getTime() < toDateTime.getTime())) {
                toDateTime = toDateTimeMax;
            }
        }
        if (toDateTime != null && fromDateTime == null) {
            fromDateTime = DateTimeUtil.getNewDateWithIntervalDays(toDateTime, -29);
        }

        ModelAndView view = new ModelAndView();
        view.setViewName("report/overall-inline");
        view.addObject("overallInlines", overallInlines);
        view.addObject("fromDate", fromDateTime);
        view.addObject("toDate", toDateTime);
        view.addObject("active", "ufs-report");
        view.addObject("activeTable", "ufs-overallInline");

        return view;
    }

    @RequestMapping(value = "/overall/inline/data", method = RequestMethod.GET)
    @ResponseBody
    public RespData getOverallInlineReportData(String fromDate, String toDate) {
        RespData resp = new RespData();

        Date fromDateTime = DateTimeUtil.getDateFromDateTimeString(fromDate);
        Date toDateTime = DateTimeUtil.getDateFromDateTimeString(toDate);

        if (fromDateTime != null) {
            Date toDateTimeMax = DateTimeUtil.getNewDateWithIntervalDays(fromDateTime, 29);
            if (toDateTime == null || (toDateTime != null && toDateTimeMax.getTime() < toDateTime.getTime())) {
                toDateTime = toDateTimeMax;
            }
        }
        if (toDateTime != null && fromDateTime == null) {
            fromDateTime = DateTimeUtil.getNewDateWithIntervalDays(toDateTime, -29);
        }

        List<OverallInlineVo> overallInlines = sessionService.findOverallInlineReport(fromDateTime, toDateTime);
        resp.setData(overallInlines);

        return resp;
    }

    @RequestMapping(value = "/overall/inline/export", method = RequestMethod.GET)
    @ResponseBody
    public void exportOverallInlineReportData(String fromDate, String toDate, HttpServletResponse response)
            throws Exception {
        Date fromDateTime = DateTimeUtil.getDateFromDateTimeString(fromDate);
        Date toDateTime = DateTimeUtil.getDateFromDateTimeString(toDate);

        if (fromDateTime != null) {
            Date toDateTimeMax = DateTimeUtil.getNewDateWithIntervalDays(fromDateTime, 29);
            if (toDateTime == null || (toDateTime != null && toDateTimeMax.getTime() < toDateTime.getTime())) {
                toDateTime = toDateTimeMax;
            }
        }
        if (toDateTime != null && fromDateTime == null) {
            fromDateTime = DateTimeUtil.getNewDateWithIntervalDays(toDateTime, -29);
        }

        List<OverallInlineVo> overallInlines = sessionService.findOverallInlineReport(fromDateTime, toDateTime);

        Resource resource = appContext.getResource("classpath:report/overall-inline.xlsx");
        if (resource != null && resource.exists()) {
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition",
                    "attachment;filename=" + URLEncoder.encode("总体进线量报表.xlsx", "UTF-8"));
            Context xlsContext = new Context();
            xlsContext.putVar("rows", overallInlines);
            try {
                JxlsHelper.getInstance().processTemplate(resource.getInputStream(), response.getOutputStream(),
                        xlsContext);
            } catch (IOException e) {
                throw e;
            }
        } else {
            throw new NotFoundException();
        }
    }

    @RequestMapping(value = "/dissatisfied/export", method = RequestMethod.GET)
    @ResponseBody
    public void exportDissatisfiedDetailReport(String[] agentIds, String fromDate, String toDate,
            HttpServletResponse response) throws Exception {
        if (agentIds == null) {
            agentIds = new String[0];
        }

        Date fromDateTime = DateTimeUtil.getDateFromDateTimeString(fromDate);
        Date toDateTime = DateTimeUtil.getDateFromDateTimeString(toDate);

        if (fromDateTime != null) {
            Date toDateTimeMax = DateTimeUtil.getNewDateWithIntervalDays(fromDateTime, 29);
            if (toDateTime == null || (toDateTime != null && toDateTimeMax.getTime() < toDateTime.getTime())) {
                toDateTime = toDateTimeMax;
            }
        }
        if (toDateTime != null && fromDateTime == null) {
            fromDateTime = DateTimeUtil.getNewDateWithIntervalDays(toDateTime, -29);
        }
        List<String> agentIdsData = Arrays.asList(agentIds);
        List<DissatisfiedData> dissatisfiedDatas = sessionService.findDissatisfiedDetailReport(agentIdsData,
                fromDateTime, toDateTime, true);

        Resource resource = appContext.getResource("classpath:report/dissatisfied-detail.xlsx");
        if (resource != null && resource.exists()) {
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition",
                    "attachment;filename=" + URLEncoder.encode("不满意客户明细.xlsx", "UTF-8"));
            Context xlsContext = new Context();
            xlsContext.putVar("rows", dissatisfiedDatas);
            try {
                JxlsHelper.getInstance().processTemplate(resource.getInputStream(), response.getOutputStream(),
                        xlsContext);
            } catch (IOException e) {
                throw e;
            }
        } else {
            throw new NotFoundException();
        }
    }

}
