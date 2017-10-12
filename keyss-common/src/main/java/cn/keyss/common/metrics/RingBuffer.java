package cn.keyss.common.metrics;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 环状缓存
 */
public class RingBuffer implements AutoCloseable {
	// 时间窗口大小
	long timeWindowTicket;
	// 桶数量
	int bucketNumber;
	// 每个桶的时间长度
	long bucketTimeStamp;
	// 读缓存
	Bucket readerBucket;
	// 写桶
	Bucket[] writeBucket;
	// 读索引
	int readerNextIndex;
	//是否已关闭
	boolean isClosed;
	private static Logger logger = LoggerFactory.getLogger(RingBuffer.class);

	/**
	 * 构造环状缓存
	 *
	 * @param timeWindowTickets
	 * @param bucketNumber
	 */
	public RingBuffer(long timeWindowTickets, int bucketNumber) {
		this.timeWindowTicket = timeWindowTickets;
		this.bucketNumber = bucketNumber;
		this.bucketTimeStamp = timeWindowTicket / bucketNumber;
		long currentTime = System.currentTimeMillis();
		readerNextIndex = getCurrentIndex(currentTime);
		writeBucket = new Bucket[this.bucketNumber];

		for (int i = 0; i < this.bucketNumber; i++) {
			int index = (readerNextIndex + i) % bucketNumber;
			writeBucket[index] = Bucket.createBucket();
			this.resetBucket(writeBucket[index], index);
			logger.trace("初始化第{}个桶", index);
		}
		readerBucket = Bucket.createBucket();
		logger.trace("初始化读桶");
	}

	/**
	 * 获取可读桶
	 * 
	 * @return
	 * @throws InterruptedException
	 */
	public Bucket next() throws InterruptedException {
		Bucket nextBucket = writeBucket[readerNextIndex];
		if (nextBucket.canWrite(System.currentTimeMillis()))
			return null;
		this.resetBucket(readerBucket, readerNextIndex);
		writeBucket[readerNextIndex] = readerBucket;
		readerBucket = nextBucket;
		readerIndexToNext();
		return readerBucket;
	}

	/**
	 * 阻塞当前线程直到获取可读桶
	 * 
	 * @throws InterruptedException
	 */
	public Bucket nextInBlock() throws InterruptedException {
		Bucket nextBucket = writeBucket[readerNextIndex];
		while (nextBucket.canWrite(System.currentTimeMillis())) {
			long sleepMillis = nextBucket.getStopTickets() - System.currentTimeMillis();
			if (sleepMillis > 0) {
				Thread.sleep(sleepMillis);
			}
		}
		this.resetBucket(readerBucket, readerNextIndex);
		writeBucket[readerNextIndex] = readerBucket;
		readerBucket = nextBucket;
		readerIndexToNext();
		return readerBucket;
	}

	/**
	 * 读取当前正在写的数据
	 * 
	 * @return
	 */
	public Bucket readCurrent() throws MetricException {
		if (!isClosed)
			throw new MetricException("正在写入数据，不能读取当前桶");
		return writeBucket[readerNextIndex];
	}

	void readerIndexToNext() {
		readerNextIndex++;
		if (readerNextIndex >= bucketNumber)
			readerNextIndex = 0;
	}

	/**
	 * 写入数据
	 * 
	 * @param info
	 */
	public void add(MetricInfo info) throws MetricException {
		if (isClosed)
			throw new MetricException("已经停止接收数据");
		long currentTickets = System.currentTimeMillis();
		int bucketIndex = getCurrentIndex(currentTickets);
		Bucket result = writeBucket[bucketIndex];
		if (result.canWrite(currentTickets)) {
			result.writeData(info);
		} else {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
			logger.warn("写数据被丢弃,目标桶编号：{}，桶起止时间:[{}/{}]，当前时间:{}", bucketIndex,
					simpleDateFormat.format(new Date(result.getStartTickets())),
					simpleDateFormat.format(new Date(result.getStopTickets())),
					simpleDateFormat.format(new Date(currentTickets)));
		}
	}

	/**
	 * 桶数据汇报后，重设桶
	 *
	 * @param bucket
	 * @param index
	 */
	private void resetBucket(Bucket bucket, int index) {
		long currentTime = System.currentTimeMillis();
		long startTime = getBaseTime(currentTime) + index * bucketTimeStamp;
		long stopTime = startTime + bucketTimeStamp;
		// 如果重置生成的桶不可用，则下推一圈
		while (stopTime <= currentTime) {
			startTime = startTime + timeWindowTicket;
			stopTime = startTime + bucketTimeStamp;
		}
		bucket.reset(startTime, stopTime);
	}

	/**
	 * 计算当前桶
	 * 
	 * @return
	 */
	public int getCurrentIndex(long currentTime) {
		long index = getOffset(currentTime) / bucketTimeStamp;
		return (int) index;
	}

	/**
	 * 当前时间相对baseTime的偏移;
	 * 
	 * @param currentTime
	 * @return
	 */
	public long getOffset(long currentTime) {
		return currentTime % timeWindowTicket;
	}

	/**
	 * RingBuffer基准时间
	 * 
	 * @param currentTime
	 * @return
	 */
	public long getBaseTime(long currentTime) {
		return currentTime - getOffset(currentTime);
	}

	@Override
	public void close() {
		isClosed = true;
	}
}
