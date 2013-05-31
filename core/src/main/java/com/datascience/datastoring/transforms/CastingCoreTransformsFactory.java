package com.datascience.datastoring.transforms;

import com.datascience.core.base.AssignedLabel;
import com.datascience.core.base.LObject;
import com.datascience.core.base.Worker;
import com.datascience.core.nominal.PureNominalData;
import com.datascience.core.results.*;
import com.datascience.utils.ITransformation;
import com.datascience.utils.transformations.CastingTransform;
import com.google.gson.JsonObject;

import java.util.Collection;

public class CastingCoreTransformsFactory implements ICoreTransformsFactory<Object>{

	@Override
	public ITransformation<JsonObject, Object> createSettingsTransform() {
		return new CastingTransform<JsonObject>();
	}

	@Override
	public ITransformation<String, Object> createKindTransform() {
		return new CastingTransform<String>();
	}

	@Override
	public <T> ITransformation<Collection<AssignedLabel<T>>, Object> createAssignsTransformation() {
		return new CastingTransform<Collection<AssignedLabel<T>>>();
	}

	@Override
	public <T> ITransformation<Collection<LObject<T>>, Object> createObjectsTransformation() {
		return new CastingTransform<Collection<LObject<T>>>();
	}

	@Override
	public <T> ITransformation<Collection<Worker<T>>, Object> createWorkersTransformation() {
		return new CastingTransform<Collection<Worker<T>>>();
	}

	@Override
	public ITransformation<PureNominalData, Object> createPureNominalDataTransformation() {
		return new CastingTransform<PureNominalData>();
	}

	@Override
	public ITransformation<DatumContResults, Object> createDatumContResultsTransformation() {
		return new CastingTransform<DatumContResults>();
	}

	@Override
	public ITransformation<WorkerContResults, Object> createWorkerContResultsTransformation() {
		return new CastingTransform<WorkerContResults>();
	}

	@Override
	public ITransformation<DatumResult, Object> createDatumStringResultsTransformation() {
		return new CastingTransform<DatumResult>();
	}

	@Override
	public ITransformation<WorkerResult, Object> createWorkerStringResultsTransformation(ResultsFactory.WorkerResultNominalFactory wrnf) {
		return new CastingTransform<WorkerResult>();
	}

	@Override
	public String getID() {
		return "MEMORY";
	}
}
