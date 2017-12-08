package cn.ljh.rhino.zk;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent.Type;
import org.apache.curator.framework.recipes.cache.TreeCacheListener;

public class ZkMsgListener implements TreeCacheListener {
	MessageHandler handler;
	boolean initialized = false;

	public ZkMsgListener(MessageHandler handler) {
		this.handler = handler;
	}

	@Override
	public void childEvent(CuratorFramework client, TreeCacheEvent treeCacheEvent) throws Exception {
		if(!initialized){
			if(treeCacheEvent.getType()==Type.INITIALIZED){
				initialized = true;
				System.out.println("====================================INITIALIZED===============================");
			}
		} else {
			if (null != treeCacheEvent && treeCacheEvent.getData() != null) {
				switch (treeCacheEvent.getType()) {
				case NODE_ADDED:
					if(initialized) {
						handler.onMsg(TreeCacheEvent.Type.NODE_ADDED.name(), new String(treeCacheEvent.getData().getData()));
					}
					break;
				case NODE_UPDATED:
					if(initialized) {
						handler.onMsg(TreeCacheEvent.Type.NODE_UPDATED.name(), new String(treeCacheEvent.getData().getData()));
					}
					break;
				case NODE_REMOVED:
					if(initialized) {
						handler.onMsg(TreeCacheEvent.Type.NODE_REMOVED.name(), new String(treeCacheEvent.getData().getData()));
					}
					break;
				default:
					break;
				}
			}
		}
	}

}
