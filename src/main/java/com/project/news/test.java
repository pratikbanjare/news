package com.project.news;

public class test {
    public static void main(int[] nums, int k) {

        // int[] nums2 = new int[nums.length];
        // int checker = nums.length - k;
        // if ( k > nums.length) {
        //     checker = nums.length  - (k-nums.length)%nums.length;
        // }

        // int j = 0;
        // for (int i = checker; i < nums.length; ++i, ++j) {
        //     nums2[j] = nums[i];
        // }
        // for (int i = 0 ; i<checker; ++i,++j){
        //     nums2[j] = nums[i];
        // }

        // for (int i = 0; i<nums.length; ++i){
        //     nums[i] = nums2[i];
        // }

        // int arrayLength = nums.length;
        // int shift = k%arrayLength;
        // int[] result = new int[arrayLength];
        // int insertIndex = 0;
        // result[0] = nums[shift];
        // insertIndex++;
        // int finals = shift;
        // shift++;
        // while(finals != shift){
        //     result[insertIndex-1] = nums[shift];
        //     insertIndex++;
        //     shift = shift+1%arrayLength;
        // }

        // nums = result;


        int shift = k&nums.length;
        int[] result = new int[nums.length];
        for(int j = 0; j<nums.length; j++){
            result[j] = nums[shift];
            shift = (shift+1)%nums.length;
        }

        for ( int i = 0; i< nums.length; i++)
        {
            nums[i] = result[i];
        }
    }}
