package com.basic.solr;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;

import java.io.IOException;

/**
 * locate com.basic.solr
 * Created by mastertj on 2018/4/1.
 */
public class TestOperator {

    public static final String SOLR_URL="http://ubuntu1:8080/solr/new_core";
    private static HttpSolrClient solr = null;
    @Test
    public void testAdd() throws IOException, SolrServerException {
        //实例化Solr对象
        solr = new HttpSolrClient.Builder(SOLR_URL).withConnectionTimeout(10000).withSocketTimeout(60000).build();

        //实例化添加数据类
        SolrInputDocument document1=new SolrInputDocument();
        document1.setField("id","1001");
        document1.setField("name","iphone6S手机");
        document1.setField("price","6000");
        document1.setField("url","/images/001.jpg");

        SolrInputDocument document2=new SolrInputDocument();
        document2.setField("id","1002");
        document2.setField("name","三星手机");
        document2.setField("price","5300");
        document2.setField("url","/images/001.jpg");

        solr.add(document1);
        solr.add(document2);
        solr.commit();
    }

    @Test
    public void testSearch() throws IOException, SolrServerException {
        //实例化Solr对象
        solr = new HttpSolrClient.Builder(SOLR_URL).withConnectionTimeout(10000).withSocketTimeout(60000).build();
        //查询类
        SolrQuery query=new SolrQuery();
        //查询关键字
        query.set("q","name:手机");
        //查询数据
        QueryResponse queryResponse = solr.query(query);

        //返回数据
        SolrDocumentList results = queryResponse.getResults();
        System.out.println("条数: "+results.getNumFound());
        for(SolrDocument solrDocument : results){
            String id= (String) solrDocument.get("id");
            String name= (String) solrDocument.get("name");
            Long price= (Long) solrDocument.get("price") ;
            String url = (String) solrDocument.get("url");
            System.out.println("id: "+id+" name: "+name+" price"+price+" url: "+url);
        }
    }

    @Test
    public void testDelById() throws IOException, SolrServerException {
        //实例化Solr对象
        solr = new HttpSolrClient.Builder(SOLR_URL).withConnectionTimeout(10000).withSocketTimeout(60000).build();

        //deleteById形式删除
        solr.deleteById("1");
        solr.commit();
    }

    @Test
    public void testDelByQuery() throws IOException, SolrServerException {
        //实例化Solr对象
        solr = new HttpSolrClient.Builder(SOLR_URL).withConnectionTimeout(10000).withSocketTimeout(60000).build();

        //deleteByQuery形式删除
        solr.deleteByQuery("id:1001 id:1002");
        solr.commit();
    }
}
