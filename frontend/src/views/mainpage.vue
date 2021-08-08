<template>
  <el-row :gutter="20" justify="center" align="middle">
    <el-col :span="10">
      <el-carousel :interval="5000" arrow="always" @change="changePage">
        <el-carousel-item v-for="(item,idx) in state.items" :key="idx">
          <el-image
          :src="item.src"
          :fit="fit"></el-image>
        </el-carousel-item>
      </el-carousel>
    </el-col>
    <el-col :span="10">
    <h2>{{state.explain}}</h2>
    </el-col>
  </el-row>
</template>

<script>
import { onMounted, reactive } from "vue";
import { useStore } from "vuex";
import ex1 from "@/assets/images/ex1.jpg"
import ex2 from "@/assets/images/ex2.jpg"
import ex3 from "@/assets/images/ex3.jpg"
import ex4 from "@/assets/images/ex4.jpg"

export default {
  name: "Mainpage",
  setup() {
    const store = useStore();
    // 페이지 진입시 불리는 훅
    onMounted(() => {
      store.commit("root/setMenuActiveMenuName", "mainpage");
    });
    const state = reactive({
      items: [
        {src: ex1},
        {src: ex2},
        {src: ex3},
        {src: ex4},
      ],
      page: 0,
      explain: '빡곰스터디를 이용해 공부해보세요.'
    })
    const changePage = function(page) {
      if (page == 0) {
        state.explain = '빡곰스터디를 이용해 공부해보세요.'
      } else if (page == 1) {
        state.explain = '스터디를 생성해보세요.'
      } else if (page == 2) {
        state.explain = '열정적인 스터디원을 직접 모아보세요.'
      } else if (page == 3) {
        state.explain = '열심히 참여하고 열정도를 높여보세요.'
      }
    }

    return { state, changePage}
  }
};
</script>

<style>

</style>
