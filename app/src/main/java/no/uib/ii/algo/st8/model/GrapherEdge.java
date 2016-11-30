package no.uib.ii.algo.st8.model;

import java.io.Serializable;

/**
 * Created by deas on 30/11/16.
 */

public interface GrapherEdge<V> extends Colorful, Geometric, Serializable {
	V getSource();

	V getTarget();

	void setStyle(EdgeStyle solid);

	EdgeStyle getStyle();
}
