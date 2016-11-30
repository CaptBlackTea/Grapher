package no.uib.ii.algo.st8.model;

import no.uib.ii.algo.st8.util.Coordinate;

/**
 * Created by deas on 30/11/16.
 */

public class DefaultGrapherVertexFactory implements GrapherVertexFactory {
	@Override
	public GrapherVertex createVertex(Coordinate c) {
		return new DefaultVertex(c);
	}
}
