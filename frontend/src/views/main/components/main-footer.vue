<template>
  <el-row class="main-footer" :gutter="5">
    <div class="contents">
      PPAKGOM
    </div>
    <el-badge class="item">
      <el-button icon="el-icon-chat-round" circle type="success" plain></el-button>
    </el-badge>
  </el-row>
</template>
<style>
@media (max-width: 700px) {
  .main-footer {
    height: 50px;
  }
  .main-footer .contents {
    line-height: 50px;
    text-align: center;
    margin: 0 auto;
  }
}

@media (min-width: 701px) {
  .main-footer {
    height: 50px;
  }
  .main-footer .contents {
    line-height: 50px;
    text-align: center;
    margin: 0 auto;
  }
}
</style>
<script>
import { reactive } from "vue";
import { useStore } from "vuex";

export default {
  name: "main-footer",

  props: {
    height: {
      type: String,
      default: "110px"
    }
  },

  setup() {
    const state = reactive({});

    return { state };
  }
};
</script>
<style></style>
