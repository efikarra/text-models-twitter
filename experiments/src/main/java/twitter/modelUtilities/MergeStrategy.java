package twitter.modelUtilities;

public enum MergeStrategy {
	AGGREGATION_STRATEGY, CENTROID_STRATEGY,ROCCHIO_STRATEGY;

	public static AbstractStrategy getStrategy(MergeStrategy simMethod) {
		switch (simMethod) {
		case AGGREGATION_STRATEGY:
			return new AggregationStrategy();
		case CENTROID_STRATEGY:
			return new CentroidStrategy();
		default:
			return null;
		}
		
	}
	public static String getName(MergeStrategy simMethod) {
		switch (simMethod) {
		case AGGREGATION_STRATEGY:
			return "AGGST";
		case CENTROID_STRATEGY:
			return "CENTST";
		case ROCCHIO_STRATEGY:
			return "ROCST";
		default:
			return null;
		}
}
}
