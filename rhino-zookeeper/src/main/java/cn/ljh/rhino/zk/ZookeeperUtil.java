package cn.ljh.rhino.zk;

import java.util.List;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.TreeCache;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//监听节点子节点变更
public class ZookeeperUtil {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	CuratorFramework curator;
	
	@SuppressWarnings("resource")
	public void register(MessageHandler handler) throws Exception {
		TreeCache treeCache = new TreeCache(curator, handler.getNodeName());
		treeCache.start();
		ZkMsgListener listener = new ZkMsgListener(handler);
		treeCache.getListenable().addListener(listener);
		logger.debug("-----run----");
		new Thread() {
			public void run() {
				try {
					String waitObject = new String("waitObject");
					synchronized (waitObject) {
						waitObject.wait();
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			};
		}.start();
	}

	@SuppressWarnings({ "resource", "unused" })
	public void sendMsg(String node, String msg) throws Exception {
		// 最后一个参数表示是否进行压缩
		NodeCache cache = new NodeCache(curator, node, false);
		Stat stat = curator.checkExists().forPath(node);
		if (stat == null) {
			curator.create().forPath(node, msg.getBytes("UTF-8"));
		} else {
			curator.setData().forPath(node, msg.getBytes("UTF-8"));
		}
	}

	public void createNode(String node,CreateMode createMode) throws Exception {
		// 最后一个参数表示是否进行压缩
		Stat stat = curator.checkExists().forPath(node);
		if (stat == null) {
			curator.create().creatingParentsIfNeeded().withMode(createMode).forPath(node);
		} else {
			curator.setData().forPath(node);
		}
	}
	
	public void setNodeData(String node,String data) throws Exception {
		// 最后一个参数表示是否进行压缩
		Stat stat = curator.checkExists().forPath(node);
		if (stat == null) {
			curator.create().forPath(node,data.getBytes("UTF-8"));
		} else {
			curator.setData().forPath(node,data.getBytes("UTF-8"));
		}
	}

	public void delNode(String node) throws Exception {
		curator.delete().forPath(node);
	}

	public String getNodeData(String node) throws Exception {
		byte[] bys = curator.getData().forPath(node);
		return new String(bys,"UTF-8");
	}
	
	public List<String> nodesList(String parentPath) throws Exception{  
        List<String> paths = curator.getChildren().forPath(parentPath);  
        return paths;
    } 
	
	public void delChildrenIfNeeded(String parentPath) throws Exception{  
		curator.delete().guaranteed().deletingChildrenIfNeeded().forPath(parentPath); 
    }

	public CuratorFramework getCurator() {
		return curator;
	}

	public void setCurator(CuratorFramework curator) {
		this.curator = curator;
	}

}