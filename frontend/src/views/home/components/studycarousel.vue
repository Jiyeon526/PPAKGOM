<template>
  <el-image class="img-center" :src="state.uri" :fit="fit"> </el-image>
</template>
<style scoped>
.img-center:hover {
  transform: scale(1.05, 1.05);
  box-shadow: 5px 5px 5px rgb(143, 209, 141, 0.7);
}
.img-center {
  height: 280px;
  display: block;
  margin-left: auto;
  margin-right: auto;
  border-radius: 15px;
}
</style>
<script>
import { onMounted, reactive } from "vue";
import { useRouter } from "vue-router";
import { useStore } from "vuex";
import axios from "axios";

export default {
  name: "studycarousel",

  props: {
    studyData: {
      type: Object
    }
  },

  setup(props, { emit }) {
    const store = useStore();
    const router = useRouter();
    const state = reactive({
      uri: "",
      studyData: ""
    });

    onMounted(() => {
      state.studyData = props.studyData.study_thumbnail;
      var name;
      if (
        state.studyData.split("\\").length > state.studyData.split("/").length
      ) {
        name = state.studyData.split("\\");
      } else {
        name = state.studyData.split("/");
      }
      axios({
        url: `https://i5b306.p.ssafy.io/api/v1/study/${name[2]}/download`,
        method: "GET",
        responseType: "blob"
      }).then(res => {
        state.uri = URL.createObjectURL(res.data);
      });
    });

    return { state };
  }
};
</script>
