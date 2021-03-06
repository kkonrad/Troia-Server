package com.datascience.mv;

import com.datascience.core.base.*;
import com.datascience.core.nominal.NominalAlgorithm;
import com.datascience.core.results.WorkerResult;
import com.datascience.core.stats.ConfusionMatrixNormalizationType;
import com.datascience.core.nominal.ICategoryPriorCalculator;
import com.datascience.core.stats.IErrorRateCalculator;
import com.datascience.core.results.DatumResult;
import com.datascience.utils.ProbabilityDistributions;

import java.util.Collection;
import java.util.Map;

import static com.datascience.core.nominal.ProbabilityDistributions.generateMV_PD;

/**
 * @Author: konrad
 */
public abstract class MajorityVote extends NominalAlgorithm {

	public MajorityVote(IErrorRateCalculator errorRateCalculator, ICategoryPriorCalculator categoryPriorCalculator){
		super(errorRateCalculator, categoryPriorCalculator);
	}

	public void computeResultsForObject(LObject<String> object){
		DatumResult dr = results.getOrCreateDatumResult(object);
		dr.setCategoryProbabilites(calculateDistribution(object));
		results.addDatumResult(object, dr);
	}

	public Map<String, Double> calculateDistribution(LObject<String> object){
		Collection<String> categories = data.getCategories();
		if (object.isGold()) {
			return ProbabilityDistributions.generateGoldDistribution(categories, object.getGoldLabel());
		}
		Collection<AssignedLabel<String>> assigns = data.getAssignsForObject(object);
		if (assigns.isEmpty()) {
			return generateLabelForNonAssignedObject();
		}
		return generateLabelDistribution(categories, assigns);
	}

	public Map<String, Double> generateLabelForNonAssignedObject(){
		return getCategoryPriors();
	}

	public Map<String, Double> generateLabelDistribution(Collection<String> categories,
				Collection<AssignedLabel<String>> assigns){
		return generateMV_PD(categories, assigns);
	}

	/**
	 * from old batch DS code
	 * @param worker
	 */
	public void computeWorkersConfusionMatrix(Worker worker){
		WorkerResult wr = results.getOrCreateWorkerResult(worker);
		wr.empty();

		// Scan all objects and change the confusion matrix for each worker
		// using the class probability for each object
		for (AssignedLabel<String> al : data.getWorkerAssigns(worker)) {

			// Get the name of the object and the category it
			// is classified from this worker.
			String destination = al.getLabel();
			// We get the classification of the object
			// based on the votes of all the other workers
			// We treat this classification as the "correct" one
			Map<String, Double> probabilities = results.getDatumResult(al.getLobject()).getCategoryProbabilites();

			for (Map.Entry<String, Double> e : probabilities.entrySet()) {
				wr.addError(e.getKey(), destination, e.getValue());
			}
		}
		wr.normalize(ConfusionMatrixNormalizationType.LAPLACE);
		results.addWorkerResult(worker, wr);
	}
}
