<template>
  <el-container>
    studylike
  </el-container>
</template>

<script>
import { onMounted } from "vue";
import { useStore } from "vuex";

export default {
  name: "Studylike",
  setup() {
    const store = useStore();

    // 페이지 진입시 불리는 훅
    onMounted(() => {
      store.commit("root/setMenuActiveMenuName", "like");
    });
  }
};
</script>
