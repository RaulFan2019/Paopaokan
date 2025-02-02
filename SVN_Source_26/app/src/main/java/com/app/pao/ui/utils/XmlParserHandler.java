package com.app.pao.ui.utils;

import com.app.pao.entity.model.ProvinceBean;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;


public class XmlParserHandler extends DefaultHandler {

	/**
	 * 存储所有的解析对象
	 */
	private ArrayList<ProvinceBean> provinceList = new ArrayList<ProvinceBean>();

	public XmlParserHandler() {

	}

	public ArrayList<ProvinceBean> getProvinceDataList() {
		return provinceList;
	}


	@Override
	public void startDocument() throws SAXException {
		// 当读到第一个开始标签的时候，会触发这个方法
	}

	ProvinceBean provinceBean = new ProvinceBean();
	String cityName = "";

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		// 当遇到开始标记的时候，调用这个方法
		if (qName.equals("province")) {
			provinceBean = new ProvinceBean();
			provinceBean.setName(attributes.getValue(0));
			provinceBean.setCityList(new ArrayList<String>());
		} else if (qName.equals("city")) {
			cityName  = new String();
			cityName = attributes.getValue(0);
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		// 遇到结束标记的时候，会调用这个方法
		if (qName.equals("city")) {
			provinceBean.getCityList().add(cityName);
		} else if (qName.equals("province")) {
			provinceList.add(provinceBean);
		}
	}

	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
	}

}
