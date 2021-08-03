<template>
  <el-container>
    Createpage
  </el-container>
</template>

<script>
import { onMounted } from "vue";
import { useStore } from "vuex";

export default {
  name: "Createpage",
  setup() {
    const store = useStore();

    // 페이지 진입시 불리는 훅
    onMounted(() => {
      store.commit("root/setMenuActiveMenuName", "create");
    });
  }
};
</script>
