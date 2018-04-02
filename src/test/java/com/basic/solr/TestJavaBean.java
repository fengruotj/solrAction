package com.basic.solr;

import com.basic.solr.model.User;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.UUID;

/**
 * locate com.basic.solr
 * Created by mastertj on 2018/4/2.
 */
public class TestJavaBean {
    public static final String SOLR_URL="http://ubuntu1:8080/solr/new_core";
    private HttpSolrClient solr = null;

    @Before
    public void conn() throws IOException, SolrServerException {

        //实例化Solr对象
        solr = new HttpSolrClient.Builder(SOLR_URL).withConnectionTimeout(10000).withSocketTimeout(60000).build();
    }

    @Test
    public void testAddUser() throws IOException, SolrServerException {
        String prefix="user_";
        User user1=new User();
        user1.setId(prefix + UUID.randomUUID().toString().substring(4).substring(prefix.length()));
        user1.setName("谭杰");
        user1.setAge(23);
        user1.setSex("男");
        user1.setLikes(new String[]{"篮球","打游戏"});
        solr.addBean(user1);

        User user2=new User();
        user1.setId(prefix + UUID.randomUUID().toString().substring(4).substring(prefix.length()));
        user1.setName("刘聪");
        user1.setAge(23);
        user1.setSex("男");
        user1.setLikes(new String[]{"学习","打游戏"});
        solr.addBean(user1);

        solr.commit();
    }

    /**
     * solrDocument转换为User模型类
     */
    @Test
    public void testChange(){
        SolrDocumentList solrDocumentList=new SolrDocumentList();
        SolrDocument solrDocument=new SolrDocument();
        solrDocument.setField("id","123456");
        solrDocument.setField("user_name","名称");
        solrDocument.setField("user_like",new String[]{"学习","打游戏"});
        solrDocument.setField("user_sex","女");
        solrDocument.setField("user_age",18);
        solrDocumentList.add(solrDocument);
        User user=solr.getBinder().getBean(User.class, solrDocument);
        System.out.println(user);
    }

    /**
     * BeanSearch User类
     * @throws IOException
     * @throws SolrServerException
     */
    @Test
    public void testSearchUser() throws IOException, SolrServerException {
        //实例化Solr对象
        solr = new HttpSolrClient.Builder(SOLR_URL).withConnectionTimeout(10000).withSocketTimeout(60000).build();
        //查询类
        SolrQuery query=new SolrQuery();
        //查询关键字
        query.set("q","user_name:刘");
        //查询数据
        QueryResponse queryResponse = solr.query(query);

        //返回数据
        SolrDocumentList results = queryResponse.getResults();
        System.out.println("条数: "+results.getNumFound());
        for(SolrDocument solrDocument : results){
            User user=solr.getBinder().getBean(User.class, solrDocument);
            System.out.println(user);
        }
    }

    /**
     * 根据Query进行删除
     * @throws IOException
     * @throws SolrServerException
     */
    @Test
    public void testDelByQuery() throws IOException, SolrServerException {
        //实例化Solr对象
        solr = new HttpSolrClient.Builder(SOLR_URL).withConnectionTimeout(10000).withSocketTimeout(60000).build();

        //deleteByQuery形式删除
        solr.deleteByQuery("user_name:谭杰 user_name:刘");
        solr.commit();
    }
}
