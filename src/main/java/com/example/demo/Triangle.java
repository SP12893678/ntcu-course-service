package shape;

import java.util.Arrays;

public class Triangle { 
	public enum Type {
	    銳角三角形, 直角三角形, 鈍角三角形, 等腰三角形, 正三角形, 不能構成三角形
	}
	float length_a,length_b,length_c;
	
	public Triangle(float length_a,float length_b,float length_c) {
		this.length_a = length_a;
		this.length_b = length_b;
		this.length_c = length_c;
	}
	
	public Type getType() {
		float[] sideLength = {length_a,length_b,length_c};
		Arrays.sort(sideLength);
		
		if(sideLength[0] + sideLength[1] <= sideLength[2]) return Type.不能構成三角形;
		else if(sideLength[0] == sideLength[1] && sideLength[1] == sideLength[2]) return Type.正三角形;
		else if(sideLength[0] == sideLength[1] || sideLength[1] == sideLength[2]) return Type.等腰三角形;
		else if(Math.pow(sideLength[0],2) + Math.pow(sideLength[1],2) > Math.pow(sideLength[2],2)) return Type.銳角三角形;
		else if(Math.pow(sideLength[0],2) + Math.pow(sideLength[1],2) < Math.pow(sideLength[2],2)) return Type.鈍角三角形;
		else return Type.直角三角形;
	}
}