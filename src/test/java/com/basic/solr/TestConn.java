package com.basic.solr;

import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.junit.Test;

/**
 * locate com.basic.solr
 * Created by mastertj on 2018/4/1.
 */
public class TestConn {
    public static final String SOLR_URL="http://ubuntu2:8080/solr";
    private static HttpSolrClient solr = null;

    @Test
    public void conn(){
        //实例化Solr对象
        solr = new HttpSolrClient.Builder(SOLR_URL).withConnectionTimeout(10000).withSocketTimeout(60000).build();
    }
}
