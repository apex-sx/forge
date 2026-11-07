package com.github.zhitron.primitive_extension;


/**
 * Float数组
 *
 * @author zhitron
 */
public class FloatArray extends FloatTuple {

    public FloatArray() {
        super(new float[0]);
    }

    public FloatArray(float[] elements) {
        super(elements);
    }

    public final void get(FloatArray input) {
        if (size() != input.size()) {
            throw new IllegalArgumentException(
                    "It is inconsistent with the target length, the expected length is ["
                            + size() + "], and the actual length is [" + input.size() + "].");
        }
        input.eachCompute(this, (thisData, inputData) -> inputData);
    }

    public final float set(int index, float value) {
        index = Helper.idx(size(), index);
        float oldValue = elements[index];
        elements[index] = value;
        return oldValue;
    }

    public final void set(float[] input) {
        if (size() != input.length) {
            throw new IllegalArgumentException(
                    "It is inconsistent with the target length, the expected length is ["
                            + size() + "], and the actual length is [" + input.length + "].");
        }
        System.arraycopy(elements, 0, input, 0, size());
    }

    public final void set(FloatArray input) {
        if (size() != input.size()) {
            throw new IllegalArgumentException(
                    "It is inconsistent with the target length, the expected length is ["
                            + size() + "], and the actual length is [" + input.size() + "].");
        }
        this.eachCompute(input, (thisData, inputData) -> inputData);
    }

    /**
     * 创建一个当前 {@code FloatArray} 对象的副本。
     * <p>
     * 该方法通过调用 {@code toArray()} 方法获取当前元组的所有元素，并使用这些元素创建一个新的 {@code FloatArray} 对象。
     *
     * @return 返回一个新的 {@code FloatArray} 对象，该对象包含与当前元组相同的元素。
     */
    public final FloatArray toArrayObject() {
        return new FloatArray(toArray());
    }

    public final FloatArray absolute() {
        return this.eachCompute((thisData) -> thisData < 0 ? -thisData : thisData);
    }

    public final FloatArray absolute(FloatArray input) {
        return this.eachCompute(input, (thisData, inputData) -> inputData < 0 ? -inputData : inputData);
    }

    public final FloatArray negate() {
        return this.eachCompute((thisData) -> thisData > 0 ? -thisData : thisData);
    }

    public final FloatArray negate(FloatArray input) {
        return this.eachCompute(input, (thisData, inputData) -> inputData > 0 ? -inputData : inputData);
    }

    public final FloatArray plus(float data) {
        return this.eachCompute((thisData) -> thisData + data);
    }

    public final FloatArray plus(FloatArray input) {
        return this.eachCompute(input, (thisData, inputData) -> thisData + inputData);
    }

    public final FloatArray plus(FloatArray input, FloatArray value) {
        return this.eachCompute(input, value, (thisData, inputData, valueData) -> inputData + valueData);
    }

    public final FloatArray plusScale(FloatArray input, float inputScale) {
        return this.eachCompute(input, (thisData, inputData) -> thisData + inputData * inputScale);
    }

    public final FloatArray plusScale(float thisScale, FloatArray input, float inputScale) {
        return this.eachCompute(input, (thisData, inputData) -> thisData * thisScale + inputData * inputScale);
    }

    public final FloatArray plusScale(FloatArray input, float inputScale, FloatArray value, float valueScale) {
        return this.eachCompute(input, value, (thisData, inputData, valueData) -> inputData * inputScale + valueData * valueScale);
    }

    public final FloatArray mul(float data) {
        return this.eachCompute((thisData) -> thisData * data);
    }

    public final FloatArray mul(FloatArray input) {
        return this.eachCompute(input, (thisData, inputData) -> thisData * inputData);
    }

    public final FloatArray mul(FloatArray input, FloatArray value) {
        return this.eachCompute(input, value, (thisData, inputData, valueData) -> inputData * valueData);
    }

    public final FloatArray mulScale(FloatArray input, float inputScale) {
        return this.eachCompute(input, (thisData, inputData) -> thisData * inputData * inputScale);
    }

    public final FloatArray mulScale(float thisScale, FloatArray input, float inputScale) {
        return this.eachCompute(input, (thisData, inputData) -> thisData * thisScale * inputData * inputScale);
    }

    public final FloatArray mulScale(FloatArray input, float inputScale, FloatArray value, float valueScale) {
        return this.eachCompute(input, value, (thisData, inputData, valueData) -> inputData * inputScale * valueData * valueScale);
    }

    public final FloatArray minus(float data) {
        return this.eachCompute((thisData) -> thisData - data);
    }

    public final FloatArray minus(FloatArray input) {
        return this.eachCompute(input, (thisData, inputData) -> thisData - inputData);
    }

    public final FloatArray minus(FloatArray input, FloatArray value) {
        return this.eachCompute(input, value, (thisData, inputData, valueData) -> inputData - valueData);
    }

    public final FloatArray minusScale(FloatArray input, float inputScale) {
        return this.eachCompute(input, (thisData, inputData) -> thisData - inputData * inputScale);
    }

    public final FloatArray minusScale(float thisScale, FloatArray input, float inputScale) {
        return this.eachCompute(input, (thisData, inputData) -> thisData * thisScale - inputData * inputScale);
    }

    public final FloatArray minusScale(FloatArray input, float inputScale, FloatArray value, float valueScale) {
        return this.eachCompute(input, value, (thisData, inputData, valueData) -> inputData * inputScale - valueData * valueScale);
    }

    public final FloatArray div(float data) {
        return this.eachCompute((thisData) -> thisData / data);
    }

    public final FloatArray div(FloatArray input) {
        return this.eachCompute(input, (thisData, inputData) -> thisData / inputData);
    }

    public final FloatArray div(FloatArray input, FloatArray value) {
        return this.eachCompute(input, value, (thisData, inputData, valueData) -> inputData / valueData);
    }

    public final FloatArray divScale(FloatArray input, float inputScale) {
        return this.eachCompute(input, (thisData, inputData) -> thisData / inputData * inputScale);
    }

    public final FloatArray divScale(float thisScale, FloatArray input, float inputScale) {
        return this.eachCompute(input, (thisData, inputData) -> thisData * thisScale / inputData * inputScale);
    }

    public final FloatArray divScale(FloatArray input, float inputScale, FloatArray value, float valueScale) {
        return this.eachCompute(input, value, (thisData, inputData, valueData) -> inputData * inputScale / valueData * valueScale);
    }

    public final FloatArray toUnitVector() {
        double thisModulo = this.modulo();
        return this.eachCompute(thisData -> thisData / ((Number) thisModulo).floatValue());
    }

    public final FloatArray interpolate(FloatArray input, float alpha) {
        return this.eachCompute(input, (thisData, inputData) -> (1 - alpha) * thisData + alpha * inputData);
    }

    public final FloatArray clamp(float min, float max) {
        return this.eachCompute((thisData) -> thisData < min ? min : (thisData > max ? max : thisData));
    }

    public final double modulo() {
        double data = this.sum((thisData) -> thisData * thisData);
        return Math.sqrt(data);
    }

    public final double dotProduct(FloatArray input) {
        return this.sum(input, (thisData, inputData) -> thisData * inputData);
    }

    public final double angle(FloatArray input) {
        double data = this.dotProduct(input);
        double thisModulo = this.modulo(), inputModulo = input.modulo();
        return Math.acos(data / (thisModulo * inputModulo));
    }

    public final double distance(FloatArray input) {
        return this.distance(input, false);
    }

    public final double distance(FloatArray input, boolean squared) {
        double value = this.sum(input, (v, t) -> (v - t) * (v - t));
        return squared ? value : Math.sqrt(value);
    }

    public final double pointLength(FloatArray input) {
        return this.sum(input, (thisData, inputData) -> Math.abs(thisData - inputData));
    }

    /**
     * 对当前数组中的每个元素应用指定的操作，并将结果存储回原数组。
     *
     * @param function 一个单参数操作函数，用于对数组中的每个元素进行操作。
     *                 该函数的 `apply` 方法接受一个元素并返回操作后的结果。
     * @return 当前数组，用于链式调用。
     */
    public final FloatArray eachCompute(FloatDataSiOperator function) {
        int length = size();
        float[] array = new float[length];

        // 对数组中的每个元素应用指定的操作，并将结果存储到新数组中
        for (int i = 0; i < length; i++) {
            array[i] = function.apply(elements[i]);
        }

        // 将新数组中的结果复制回原数组
        System.arraycopy(array, 0, elements, 0, length);
        return this;
    }

    /**
     * 对当前数组与另一个数组中的每个对应元素应用指定的二元操作，并将结果存储在当前数组中。
     *
     * @param value    输入的数组，其长度必须与当前数组长度一致。
     * @param function 一个二元操作函数，用于对两个数组中的对应元素进行操作并返回结果。
     * @return 当前数组，用于链式调用。
     * @throws IllegalArgumentException 如果输入数组的长度与当前数组长度不一致，则抛出此异常。
     */
    public final FloatArray eachCompute(FloatArray value, FloatDataBiOperator function) {
        int length = size();
        // 检查输入数组的长度是否与当前数组长度一致
        if (length != value.size()) {
            throw new IllegalArgumentException("The length is inconsistent");
        }

        // 创建一个新的数组来存储操作结果
        float[] array = new float[length];

        // 对每个对应元素应用二元操作，并将结果存储在新数组中
        for (int i = 0; i < length; i++) {
            array[i] = function.apply(elements[i], value.elements[i]);
        }

        // 将新数组中的结果复制回当前数组
        System.arraycopy(array, 0, elements, 0, length);
        return this;
    }

    /**
     * 对当前数组与另外两个数组中的每个元素应用指定的三元操作，并将结果存储在当前数组中。
     *
     * @param value    第一个输入数组，作为操作的输入之一。
     * @param other    第二个输入数组，作为操作的输入之二。
     * @param function 一个三元操作函数，用于对三个数组中的对应元素进行操作并返回结果。
     * @return 当前数组，用于链式调用。
     * @throws IllegalArgumentException 如果输入的数组长度不一致，则抛出此异常。
     */
    public final FloatArray eachCompute(FloatArray value, FloatArray other, FloatDataTrOperator function) {
        int length = size();

        // 检查输入数组的长度是否一致，如果不一致则抛出异常
        if (length != value.size() || length != other.size()) {
            throw new IllegalArgumentException("The length is inconsistent");
        }

        // 创建一个新的数组，用于存储操作结果
        float[] array = new float[length];

        // 对每个元素应用指定的操作，并将结果存储在新数组中
        for (int i = 0; i < length; i++) {
            array[i] = function.apply(elements[i], value.elements[i], other.elements[i]);
        }

        // 将新数组中的结果复制回当前数组
        System.arraycopy(array, 0, elements, 0, length);
        return this;
    }

}
