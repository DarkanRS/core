// This program is free software: you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation, either version 3 of the License, or
// (at your option) any later version.
// 
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
// 
// You should have received a copy of the GNU General Public License
// along with this program.  If not, see <http://www.gnu.org/licenses/>.
//
//  Copyright (C) 2021 Trenton Kress
//  This file is part of project: Darkan
//
package com.rs.lib.util;

public class Rational {

	private long num, denom;

	public Rational(double d) {
		String s = String.valueOf(d);
		int digitsDec = s.length() - 1 - s.indexOf('.');

		int denom = 1;
		for (int i = 0; i < digitsDec; i++) {
			d *= 10;
			denom *= 10;
		}
		int num = (int) Math.round(d);

		this.num = num;
		this.denom = denom;
	}

	public Rational(long num, long denom) {
		this.num = num;
		this.denom = denom;
	}

	public long getNum() {
		return num;
	}

	public long denom() {
		return denom;
	}

	public String toString() {
		return Utils.asFraction(num, denom);
	}

	public static Rational toRational(double number) {
		return toRational(number, 4);
	}

	public static Rational toRational(double number, int largestRightOfDecimal) {

		long sign = 1;
		if (number < 0) {
			number = -number;
			sign = -1;
		}

		final long SECOND_MULTIPLIER_MAX = (long) Math.pow(10, largestRightOfDecimal - 1);
		final long FIRST_MULTIPLIER_MAX = SECOND_MULTIPLIER_MAX * 10L;
		final double ERROR = Math.pow(10, -largestRightOfDecimal - 1);
		long firstMultiplier = 1;
		long secondMultiplier = 1;
		boolean notIntOrIrrational = false;
		long truncatedNumber = (long) number;
		Rational rationalNumber = new Rational((long) (sign * number * FIRST_MULTIPLIER_MAX), FIRST_MULTIPLIER_MAX);

		double error = number - truncatedNumber;
		while ((error >= ERROR) && (firstMultiplier <= FIRST_MULTIPLIER_MAX)) {
			secondMultiplier = 1;
			firstMultiplier *= 10;
			while ((secondMultiplier <= SECOND_MULTIPLIER_MAX) && (secondMultiplier < firstMultiplier)) {
				double difference = (number * firstMultiplier) - (number * secondMultiplier);
				truncatedNumber = (long) difference;
				error = difference - truncatedNumber;
				if (error < ERROR) {
					notIntOrIrrational = true;
					break;
				}
				secondMultiplier *= 10;
			}
		}

		if (notIntOrIrrational) {
			rationalNumber = new Rational(sign * truncatedNumber, firstMultiplier - secondMultiplier);
		}
		return rationalNumber;
	}
}