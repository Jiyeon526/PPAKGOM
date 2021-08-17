<template>
  <el-card :body-style="{ padding: '0px' }">
    <el-image
      style="width: 100%; height: 250px;"
      :src="state.uri"
      :fit="fit"
    >
    </el-image>
  </el-card>
</template>
<style scoped>
.el-card {
  width: 300px;
  height: auto;
  margin-bottom: 40px;
  border-radius: 10px;
  border: none;
}
.el-card:hover {
  transform: scale(1.05, 1.05);
  box-shadow: 5px 5px 5px rgb(143, 209, 141, 0.7);
}
.el-card .image-wrapper {
  width: 300px;
  height: auto;
  margin: 0px;
  text-align: center;
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
      uri: '',
      studyData: "",
    });

     onMounted(() => {
      state.studyData = props.studyData.study_thumbnail;
      console.log(state.studyData);
      var name;
      if (
        state.studyData.split("\\").length > state.studyData.split("/").length
      ) {
        name = state.studyData.split("\\");
      } else {
        name = state.studyData.split("/");
      }

      console.log(name);
      //name = "9-kakao.jpg";
      axios({
        url: `https://localhost:8443/api/v1/study/${name[2]}/download`,
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
