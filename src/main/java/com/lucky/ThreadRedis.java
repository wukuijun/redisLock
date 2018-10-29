/**
 * 功能说明:
 * 功能作者:
 * 创建日期:
 * 版权归属:每特教育|蚂蚁课堂所有 www.itmayiedu.com
 */
package com.lucky;

/**
 *
 */
public class ThreadRedis extends Thread {
	private LockService lockService;

	public ThreadRedis(LockService lockService) {
		this.lockService = lockService;
	}

	@Override
	public void run() {
		lockService.seckill();

	}

}
