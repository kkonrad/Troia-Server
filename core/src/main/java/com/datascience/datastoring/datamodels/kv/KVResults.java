package com.datascience.datastoring.datamodels.kv;

import com.datascience.core.base.LObject;
import com.datascience.core.base.Worker;
import com.datascience.core.results.AbstractResults;
import com.datascience.core.results.ResultsFactory;
import com.datascience.datastoring.adapters.kv.ISafeKVStorage;


/**
 * @Author: konrad
 */
public class KVResults<T, U, V> extends AbstractResults<T, U, V> {

	protected ISafeKVStorage<U> datumKV;
	protected ISafeKVStorage<V> workersKV;

	public KVResults(ResultsFactory.DatumResultCreator<U> datumCreator, ResultsFactory.WorkerResultCreator<V> workerCreator,
					 ISafeKVStorage<U> datumKV, ISafeKVStorage<V> workersKV) {
		super(datumCreator, workerCreator);
		this.datumKV = datumKV;
		this.workersKV = workersKV;
	}

	@Override
	public U uncheckedGetDatumResults(LObject<T> obj) {
		return datumKV.get(obj.getName());
	}

	@Override
	public V uncheckedGetWorkerResult(Worker worker) {
		return workersKV.get(worker.getName());
	}

	@Override
	public boolean hasDatumResult(LObject<T> obj) {
		return datumKV.contains(obj.getName());
	}

	@Override
	public void addDatumResult(LObject<T> obj, U result) {
		datumKV.put(obj.getName(), result);
	}

	@Override
	public boolean hasWorkerResult(Worker worker) {
		return workersKV.contains(worker.getName());
	}

	@Override
	public void addWorkerResult(Worker worker, V result) {
		workersKV.put(worker.getName(), result);
	}
}
