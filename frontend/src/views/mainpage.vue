<template>
  <el-row :gutter="20" justify="center" align="middle" style="margin-top: 150px">
    <el-col :span="12">
      <el-carousel
        class="carousel-margin"
        :interval="5000"
        arrow="always"
        @change="changePage"
        height="450"
      >
        <el-carousel-item v-for="(item, idx) in state.items" :key="idx">
          <el-image :src="item.src"></el-image>
        </el-carousel-item>
      </el-carousel>
    </el-col>
    <el-col :span="8">
      <h2>{{ state.explain }}</h2>
    </el-col>
  </el-row>
</template>

<script>
import { onMounted, reactive } from "vue";
import { useStore } from "vuex";
import ex1 from "@/assets/images/ex1.gif";
import ex2 from "@/assets/images/ex2.gif";
import ex3 from "@/assets/images/ex3.gif";
import ex4 from "@/assets/images/ex4.gif";
import ex5 from "@/assets/images/ex5.gif";
import ex6 from "@/assets/images/ex6.gif";
import ex7 from "@/assets/images/ex7.gif";
import ex8 from "@/assets/images/ex8.gif";
import ex9 from "@/assets/images/ex9.gif";

export default {
  name: "Mainpage",
  setup() {
    const store = useStore();
    // 페이지 진입시 불리는 훅
    onMounted(() => {
      store.commit("root/setMenuActiveMenuName", "mainpage");
    });
    const state = reactive({
      items: [{ src: ex1 }, { src: ex2 }, { src: ex3 }, { src: ex4 },
              { src: ex5 }, { src: ex6 }, { src: ex7 }, { src: ex8 },
              { src: ex9 }],
      page: 0,
      explain: "빡곰스터디를 이용해 공부해보세요."
    });
    const changePage = function(page) {
      if (page == 0) {
        state.explain = "빡곰스터디를 이용해 공부해보세요.";
      } else if (page == 1) {
        state.explain = "관심있는 스터디를 찾아보세요.";
      } else if (page == 2) {
        state.explain = "관심있는 스터디에 참여해보세요.";
      } else if (page == 3) {
        state.explain = "스터디를 만들고 관리해보세요.";
      } else if (page == 4) {
        state.explain = "팀원을 직접 모아보세요.";
      } else if (page == 5) {
        state.explain = "일정을 정하고 팀원들과 같이 공부해보세요.";
      } else if (page == 6) {
        state.explain = "문제집을 만들고 공유해보세요.";
      } else if (page == 7) {
        state.explain = "문제를 풀고 결과를 공유해보세요.";
      } else if (page == 8) {
        state.explain = "열심히 참여하고 열정도를 높여보세요.";
      }
    };

    return { state, changePage };
  }
};
</script>

<style>
.carousel-margin {
  padding: 0px;
}
</style>
