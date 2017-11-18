package com.cyhong.controller;

import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.cyhong.common.model.Json;
import com.cyhong.model.ActuaryExtractCoeff;
import com.cyhong.model.ActuaryInsuation;
import com.cyhong.model.DataGrid;
import com.cyhong.model.TableHead;
import com.cyhong.model.User;

@RestController
@RequestMapping(value="/excelController")
public class ExcelController {

    @Autowired
    private ApplicationContext appContext;

	
	private Map<String, List<Object>> data = new HashMap<String, List<Object>>();
	
	ActuaryExtractCoeff actuaryExtractCoeff = new ActuaryExtractCoeff();
	
	ActuaryInsuation actuaryInsuation = new ActuaryInsuation();

	

    @RequestMapping(value = "/manager", method = RequestMethod.GET)
    public ModelAndView list() {
        ModelAndView view = new ModelAndView();
        view.setViewName("excelImport");
        Map<String, List<TableHead>> map = new HashMap<>();
		List<TableHead> acI = new ArrayList<TableHead>();
		TableHead th1 = new TableHead();
		th1.setFieldName("机构");
		th1.setColNo(8);
		th1.setFieldProperty("comname");
		th1.setKey("1");
		th1.setNullable("1");
		th1.setVisiable("1");
		
		TableHead th2 = new TableHead();
		th2.setFieldName("归属");
		th2.setColNo(2);
		th2.setFieldProperty("belong");
		th2.setKey("0");
		th2.setNullable("1");
		th2.setVisiable("1");
		
		TableHead th3 = new TableHead();
		th3.setFieldName("T9");
		th3.setColNo(3);
		th3.setFieldProperty("anaT9");
		th3.setKey("0");
		th3.setNullable("1");
		th3.setVisiable("1");
		
		TableHead th4 = new TableHead();
		th4.setFieldName("账套");
		th4.setColNo(4);
		th4.setFieldProperty("dbCode");
		th4.setKey("0");
		th4.setNullable("1");
		th4.setVisiable("1");
		
		acI.add(th1);
		acI.add(th2);
		acI.add(th3);
		acI.add(th4);
		map.put("ActuaryInsuation", acI);
		
		List<TableHead> acs = new ArrayList<TableHead>();
		TableHead th5 = new TableHead();
		th5.setFieldName("Sun_Name");
		th5.setColNo(1);
		th5.setFieldProperty("sunName");
		th5.setKey("1");
		th5.setNullable("1");
		th5.setVisiable("1");
		
		TableHead th6 = new TableHead();
		th6.setFieldName("RES_TYPE");
		th6.setColNo(2);
		th6.setFieldProperty("resType");
		th6.setKey("0");
		th6.setNullable("1");
		th6.setVisiable("1");
		
		TableHead th7 = new TableHead();
		th7.setFieldName("科目代码");
		th7.setColNo(3);
		th7.setFieldProperty("accAccont");
		th7.setKey("0");
		th7.setNullable("1");
		th7.setVisiable("1");
		
		acs.add(th5);
		acs.add(th6);
		acs.add(th7);

		map.put("ActuaryExtractCoeff", acs);

		view.addObject("tradeList", acs);
        return view;
    }
    
	@RequestMapping("/tableHead")
	public List<TableHead> tableHead(String tableName) {
		        
		Map<String, List<TableHead>> map = new HashMap<>();
		List<TableHead> acI = new ArrayList<TableHead>();
		TableHead th1 = new TableHead();
		th1.setFieldName("机构");
		th1.setColNo(8);
		th1.setFieldProperty("comname");
		th1.setKey("1");
		th1.setNullable("1");
		th1.setVisiable("1");
		
		TableHead th2 = new TableHead();
		th2.setFieldName("归属");
		th2.setColNo(2);
		th2.setFieldProperty("belong");
		th2.setKey("0");
		th2.setNullable("1");
		th2.setVisiable("1");
		
		TableHead th3 = new TableHead();
		th3.setFieldName("T9");
		th3.setColNo(3);
		th3.setFieldProperty("anaT9");
		th3.setKey("0");
		th3.setNullable("1");
		th3.setVisiable("1");
		
		TableHead th4 = new TableHead();
		th4.setFieldName("账套");
		th4.setColNo(4);
		th4.setFieldProperty("dbCode");
		th4.setKey("0");
		th4.setNullable("1");
		th4.setVisiable("1");
		
		acI.add(th1);
		acI.add(th2);
		acI.add(th3);
		acI.add(th4);
		map.put("ActuaryInsuation", acI);
		
		List<TableHead> acs = new ArrayList<TableHead>();
		TableHead th5 = new TableHead();
		th5.setFieldName("Sun_Name");
		th5.setColNo(1);
		th5.setFieldProperty("sunName");
		th5.setKey("1");
		th5.setNullable("1");
		th5.setVisiable("1");
		
		TableHead th6 = new TableHead();
		th6.setFieldName("RES_TYPE");
		th6.setColNo(2);
		th6.setFieldProperty("resType");
		th6.setKey("0");
		th6.setNullable("1");
		th6.setVisiable("1");
		
		TableHead th7 = new TableHead();
		th7.setFieldName("科目代码");
		th7.setColNo(3);
		th7.setFieldProperty("accAccont");
		th7.setKey("0");
		th7.setNullable("1");
		th7.setVisiable("1");
		
		acs.add(th5);
		acs.add(th6);
		acs.add(th7);

		map.put("ActuaryExtractCoeff", acs);
		System.out.println(map.get(tableName));
		return map.get(tableName);
	}

	@RequestMapping("/dataGrid")
	public DataGrid dataGrid(String tableName) {
		DataGrid dg =new DataGrid();

		actuaryExtractCoeff.setSunName("sun");
		actuaryExtractCoeff.setResType("resType");
		actuaryExtractCoeff.setAccAccont("accAccont");
		
		actuaryInsuation.setAnaT9("anaT9");
		actuaryInsuation.setBelong("belong");
		actuaryInsuation.setComname("comname");
		actuaryInsuation.setDbCode("dbCode");
		
		List<Object> acs = new ArrayList<Object>();
		acs.add(actuaryExtractCoeff);
//		Map<String, List<Object>> mapA = new HashMap<>();
		data.put("ActuaryExtractCoeff", acs);
//		data.add(mapA);
		
		List<Object> aci = new ArrayList<Object>();
		aci.add(actuaryInsuation);
//		Map<String, List<Object>> mapI = new HashMap<>();
		data.put("ActuaryInsuation", aci);
//		data.add(mapI);
				
		dg.setRows(data.get(tableName));
		System.out.println("===========");
		System.out.println(data.get(tableName));

		return dg;
	}
		
	/**
	 * 接收一个数组
	 * @param users
	 */
    @RequestMapping("/array/params")
    public Json arrayParams(@RequestBody User[] users) {
        Json json = new Json();

		System.out.println(users);
		
		System.out.println(users);
    	
    	return json;
    	
	}
	
	/**
     * 模板下载
     * 
     * @param 
     * @param filePath
     * @param response
     * @return 
     */
    @RequestMapping("/download")
    public void exportBusModeldownload(HttpServletResponse response, String tableName) {

        Json json = new Json();
        try {
//            String realPath = ConfigUtil.get("excelPath")+"downloadFile/";
//            String timeStr = DateUtil.getCurrentTimeMillis();
//            String fileName = "基础信息模板_" + "_" + DateUtil.getCurrentTimeMillis() + ".xls";
//            Resource resource = appContext.getResource("classpath:excel-config/" + tableName + ".xlsx");
            Resource resource = appContext.getResource("classpath:excel-config/" + tableName + ".xlsx");
//            Resource resource = appContext.getResource("classpath:ActuaryExtractCoeff.xlsx");

            
            System.out.println(resource.getFilename());
            System.out.println(resource.getURI());
            System.out.println(resource.getURL());

            System.out.println(resource.getFile());
            System.out.println(resource.exists());


        	System.out.println(resource);
            
            if (resource != null && resource.exists()) {
                response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
                response.setHeader("Content-Disposition",
                        "attachment;filename=" + URLEncoder.encode("ActuaryInsuation.xlsx", "UTF-8"));
//                Context xlsContext = new Context();
//                xlsContext.putVar("dates", ResponseTimes.keySet());
//                xlsContext.putVar("rows", ResponseTimes);
//                xlsContext.putVar("agents", agents);
//                try {
//                    JxlsHelper.getInstance().processTemplate(resource.getInputStream(), response.getOutputStream(),
//                            xlsContext);
//                } catch (IOException e) {
//                    throw e;
//                }
                ServletOutputStream outputStream = response.getOutputStream();

//        		InputStream in = new FileInputStream(file);
        		InputStream in = resource.getInputStream();

        		byte[] b = new byte[1024 * 1024];
        		int length;
        		while ((length = in.read(b)) > 0) {
        			outputStream.write(b, 0, length);
        		}
        		in.close();
        		outputStream.flush();
        		outputStream.close();
        		
            } else {
//                throw new NotFoundException();
            }
        	
//        	response.setContentType("application/vnd.ms-excel");
//            response.setHeader("Content-Disposition", "attachment;fileName=" + URLEncoder.encode(fileName, ConfigUtil.get("excelChaset")));
//            //导出数据
//            String[] fields = {"MappingType","input1","input2","input3","input4","input5","input6","output1","output2","output3","ReMark"};
//            //ExportBusModeldownloadUtils.exportBusModeldownload(realPath, outputStream, fields,fileName,"基础信息配置");
            json.setSuccess(true);
            json.setMsg("");
        } catch (Exception e) {
            e.printStackTrace();
            json.setSuccess(false);
            json.setMsg(e.getMessage());
//            return json;
        }
//        return json;
    }
	
	
}
