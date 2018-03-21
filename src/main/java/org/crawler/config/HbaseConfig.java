package org.crawler.config;

import java.io.IOException;

import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.HConnection;
import org.apache.hadoop.hbase.client.HConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource(value = "classpath:hbase.properties")
public class HbaseConfig {
	private static final Logger logger = LoggerFactory.getLogger(HbaseConfig.class);
	@Value("${hbase.quorum}")
	private String quorum;
	@Value("${hbase.zookeeper.port}")
	private String clientport;
	
	@Bean
	public HConnection hConnection() {
		HConnection conn = null;
		org.apache.hadoop.conf.Configuration conf = HBaseConfiguration.create();
        conf.set("hbase.zookeeper.quorum", quorum);   
        conf.set("hbase.zookeeper.property.clientPort", clientport);  
       /* try {
			conn = HConnectionManager.createConnection(conf);
		} catch (IOException e) {
			logger.error(e.getMessage());
		}*/
        return conn;
	}
}
