<template>
  <el-carousel :interval="4000" type="card" height="200px">
    <el-carousel-item v-for="item in 6" :key="item">
      <h3 class="medium">{{ item }}</h3>
    </el-carousel-item>
  </el-carousel>
  <el-row justify="center">
    여기에 소개글이 쓰입니다!
  </el-row>
</template>

<script>
import { onMounted } from "vue";
import { useStore } from "vuex";

export default {
  name: "Mainpage",
  setup() {
    const store = useStore();

    // 페이지 진입시 불리는 훅
    onMounted(() => {
      store.commit("root/setMenuActiveMenuName", "mainpage");
    });
  }
};
</script>

<style>
.el-carousel__item h3 {
  color: #475669;
  font-size: 14px;
  opacity: 0.75;
  line-height: 200px;
  margin: 0;
}

.el-carousel__item:nth-child(2n) {
  background-color: #99a9bf;
}

.el-carousel__item:nth-child(2n+1) {
  background-color: #d3dce6;
}

</style>
