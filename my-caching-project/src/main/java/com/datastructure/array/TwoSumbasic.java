package com.datastructure.array;

public class TwoSumbasic {
    public int[] twoSum(int[] nums,int target){
        for(int i=0;i<nums.length;i++){
            for(int j=i+1;j<nums.length;j++){
                if(nums[i]+nums[j]==target){
                    return new int[]{i,j};
                }
            }
        }
        return new int[]{};

    }
    public static void main(String[] args) {
        TwoSumbasic twoSum=new TwoSumbasic();
        int[] nums={2,7,11,15};
        int target=13;
        int[] result=twoSum.twoSum(nums, target);
        System.out.println(result[0]+","+result[1]);
    }
}
