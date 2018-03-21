public class Pair<V1, V2> {
	private V1 v1;
	private V2 v2;

	public Pair(V1 v1, V2 v2) {
		this.v1 = v1;
		this.v2 = v2;
	}

	public V1 getFirst() {
		return v1;
	}

	public V2 getSecond() {
		return v2;
	}
}
