package view;

public class Color {

	/**
	 * Returns the lightness of a color in a scale from 0 to 254
	 * @param color
	 * @return  lightness value of the color
	 */
	public static double[] getLightnessAndSaturation(int color) {
		double var_R = ( color >> 16 & 0xff ) / 255.0;                   //RGB from 0 to 255
		double var_G = ( color >> 8 & 0xff ) / 255.0;
		double var_B = ( color & 0xff ) / 255.0;

		double var_Min = Math.min(Math.min( var_R, var_G), var_B );    //Min. value of RGB
		double var_Max = Math.max(Math.max( var_R, var_G), var_B );      //Max. value of RGB
		double del_Max = var_Max - var_Min;
		
		double l = ( var_Max + var_Min ) / 2.0;
		
		double s;
		if ( del_Max == 0 ) {
			s = 0.0;
		}
		else {
			if ( l < 0.5 ) s = del_Max / (var_Max + var_Min );
			else 			s = del_Max / (2.0 - var_Max - var_Min);
		}
		
		double[] result = {l, s};
		return result;
	}
	
	public static double getLightness(int color) {
		double var_R = ( color >> 16 & 0xff ) / 255.0;                   //RGB from 0 to 255
		double var_G = ( color >> 8 & 0xff ) / 255.0;
		double var_B = ( color & 0xff ) / 255.0;

		double var_Min = Math.min(Math.min( var_R, var_G), var_B );    //Min. value of RGB
		double var_Max = Math.max(Math.max( var_R, var_G), var_B );      //Max. value of RGB
		
		return ( var_Max + var_Min ) / 2.0;
	}

	public static double getHue(int color) {
		double var_R = ( color >> 16 & 0xff ) / 255.0;                   //RGB from 0 to 255
		double var_G = ( color >> 8 & 0xff ) / 255.0;
		double var_B = ( color & 0xff ) / 255.0;

		double var_Min = Math.min(Math.min( var_R, var_G), var_B );    //Min. value of RGB
		double var_Max = Math.max(Math.max( var_R, var_G), var_B );      //Max. value of RGB
		double del_Max = var_Max - var_Min;             //Delta RGB value

		if ( del_Max == 0 )                     //This is a gray, no chroma...
		{
			return 0.0;                               //HSL results from 0 to 1
		}
		else                                    //Chromatic data...
		{
			double del_R = ( ( ( var_Max - var_R ) / 6 ) + ( del_Max / 2 ) ) / del_Max;
			double del_G = ( ( ( var_Max - var_G ) / 6 ) + ( del_Max / 2 ) ) / del_Max;
			double del_B = ( ( ( var_Max - var_B ) / 6 ) + ( del_Max / 2 ) ) / del_Max;

			double H = 0.0f;
			if      ( var_R == var_Max ) H = del_B - del_G;
			else if ( var_G == var_Max ) H = ( 1 / 3.0 ) + del_R - del_B;
			else if ( var_B == var_Max ) H = ( 2 / 3.0 ) + del_G - del_R;

			if ( H < 0 ) H += 1;
			if ( H > 1 ) H -= 1;
			
			return H;
		}
	}

	public static int applyHue(int color, double hue, double saturation) {
		double l = getLightness(color);
		double s = saturation;
		
		double v2;
		if ( l < 0.5 ) 		v2 = l * (1+s);
		else				v2 = ( l + s ) - ( s * l );
		
		double v1 = 2.0 * l - v2;
		//System.out.println("l: " + l + "s: " + s);
		
		int r = (int)(255 * hue_2_RGB(v1, v2, hue + (1.0/3.0)));
		int g = (int)(255 * hue_2_RGB(v1, v2, hue));
		int b = (int)(255 * hue_2_RGB(v1, v2, hue - (1.0/3.0)));
		//System.out.println("r: " + r + "g: " + g + "b: " + b);
		return (r << 16) | (g << 8) | b;
	}

	private static double hue_2_RGB(double v1, double v2, double vH) {
		if (vH < 0.0) vH += 1;
		if (vH > 1.0) vH -= 1;
		if (6 * vH < 1) return ( v1 + (v2-v1) * 6.0 * vH);
		if (2 * vH < 1) return ( v2 );
		if (3 * vH < 2) return v1 + (v2-v1) * ((2.0/3.0) - vH) * 6;
		return v1;
	}
}
