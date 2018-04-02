package com.basic.solr;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.FacetField;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.params.ModifiableSolrParams;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * locate com.basic.solr
 * Created by mastertj on 2018/4/2.
 */
public class TestMultiSearch {

    public static final String SOLR_URL="http://ubuntu1:8080/solr/new_core";
    private HttpSolrClient solr = null;
    private Random random=new Random();
    @Before
    public void conn() throws IOException, SolrServerException {

        //实例化Solr对象
        solr = new HttpSolrClient.Builder(SOLR_URL).withConnectionTimeout(10000).withSocketTimeout(60000).build();
    }

    /**
     * 添加多个Document
     */
    @Test
    public void testAddMulti() throws IOException, SolrServerException {
       for(int i=0;i<=20;i++){
           //实例化添加数据类
           SolrInputDocument document1=new SolrInputDocument();
           document1.setField("id",i);
           document1.setField("name","手机"+i);
           document1.setField("price",2000+random.nextInt(500));
           document1.setField("description","我是手机"+i);
           solr.add(document1);
    }

        for(int i=21;i<=40;i++){
            //实例化添加数据类
            SolrInputDocument document2=new SolrInputDocument();
            document2.setField("id",i);
            document2.setField("name","电脑"+i);
            document2.setField("price",2000+random.nextInt(500));
            document2.setField("description","我是电脑"+i);
            solr.add(document2);
        }

        solr.commit();
    }

    /**
     * 对Solr进行多字段查询
     */
    @Test
    public void testSearchMulit() throws IOException, SolrServerException {
        ModifiableSolrParams params=new ModifiableSolrParams();
        // 查询关键字，*:*代表所有属性设置、所有值，及所以index
        params.set("q","*:*");
        params.set("start",0);
        params.set("rows",20);
        params.set("sort","price desc");
        //返回数据

        //查询数据
        QueryResponse response = solr.query(params);
        //返回数据
        SolrDocumentList results = response.getResults();
        System.out.println("条数: "+results.getNumFound());
        for(SolrDocument solrDocument : results){
            System.out.println(solrDocument.toString());
        }
    }

    /**
     * Case查询
     */
    @Test
    public void queryCase() throws IOException, SolrServerException {
        SolrQuery solrQuery=new SolrQuery();

        //TO 条件
//        solrQuery.set("q","name:手 AND price:9");

        //TO 条件 6< price < 6000
//        solrQuery.set("q","name:手 AND price:{9 TO 6000}");

        //TO 条件 6<= price < 6000
//        solrQuery.set("q","name:手 AND price:[9 TO 6000}");

        //TO 条件 6<= price <= 6000
//        solrQuery.set("q","name:手 AND price:[9 TO 6000]");


        //TO 条件 6<= price <= 6000
        //添加过滤增加查询效率
        solrQuery.set("q","name:电脑 AND price:[9 TO 6000]");
        //solrQuery.addFilterQuery("name:电脑");

        solrQuery.set("rows",50);

        //查询数据
        QueryResponse response = solr.query(solrQuery);
        //返回数据
        SolrDocumentList results = response.getResults();
        System.out.println("条数: "+results.getNumFound());
        for(SolrDocument solrDocument : results){
            System.out.println(solrDocument.toString());
        }
    }

    /**
     * 高亮显示
     */
    @Test
    public void queryHighLight() throws IOException, SolrServerException {
        SolrQuery solrQuery=new SolrQuery();

        solrQuery.set("q","name:电脑 AND price:[9 TO 6000]");
        solrQuery.addFilterQuery("name:电脑");

        solrQuery.set("rows",50);

        //显示设置
        solrQuery.setHighlight(true);
        solrQuery.addHighlightField("name");//高亮字段
        solrQuery.setHighlightSimplePre("<font color='red'>");
        solrQuery.setHighlightSimplePost("</font>");
        solrQuery.setHighlightSnippets(1);//设置结果分片数，默认为1
        solrQuery.setHighlightFragsize(100);//设置每个分片的最大长度。默认为100
        solrQuery.setFacet(true)
                .setFacetMinCount(1)
                .setFacetLimit(5)//段
                .addFacetField("name");

        //查询数据
        QueryResponse response = solr.query(solrQuery);
        //返回数据
        SolrDocumentList results = response.getResults();
        Map<String, Map<String, List<String>>> highlighting = response.getHighlighting();
        for (String s : highlighting.keySet()) {
            Map<String, List<String>> stringListMap = highlighting.get(s);
            for (String s1 : stringListMap.keySet()) {
                System.out.println("name:"+s1+" value: "+stringListMap.get(s1));
            }
        }
        System.out.println("条数: "+results.getNumFound());
        for(SolrDocument solrDocument : results){
            //System.out.println(solrDocument.toString());
        }
    }

    /**
     * 统计分析
     * 概要说明
     */
    @Test
    public void factQueryCase() throws IOException, SolrServerException {
        SolrQuery solrQuery=new SolrQuery();

        solrQuery.set("q","name:电脑,手机 AND price:[2100 TO 6000]");

        solrQuery.set("rows",50);
        solrQuery.set("start",10);

        //Facet为solr中的层次分类查询
        //分片信息
        solrQuery.setFacet(true)
                .setFacetMinCount(1)
                .setFacetLimit(5)//段
                .setFacetPrefix("电脑") //查询description中关键字前缀是"我"的
                .addFacetField("description");
                //.addFacetField("description"); 可以查询多个字段


        //查询数据
        QueryResponse response = solr.query(solrQuery);
        //返回数据
        SolrDocumentList results = response.getResults();
        System.out.println("条数: "+results.getNumFound());
        for(SolrDocument solrDocument : results){
            System.out.println(solrDocument.toString());
        }

        List<FacetField> facetFields = response.getFacetFields();
        for(FacetField facetField:facetFields){
            List<FacetField.Count> values = facetField.getValues();
            for(FacetField.Count count : values){
                //关键字 出现次数
                System.out.println(count.getName()+" "+count.getCount());
            }
        }
    }
}
