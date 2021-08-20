<template>
  <div class="evaluate-box">
    <el-container>
      <el-header height="150px">
        <el-row :gutter="24">
          <el-col :span="16">
            <h3>{{ memberData.name }}</h3>
            <p>열정도: {{ memberData.temperature }}°C</p>
            <el-progress
              :stroke-width="20"
              :percentage="memberData.temperature"
              :show-text="false"
              style="margin-left: 10px;"
            >
            </el-progress>
          </el-col>
          <el-col :span="8">
            <el-avatar
              :src="state.uri"
              :fit="fill"
              :size="135"
              style="margin-top:15px;"
            ></el-avatar>
          </el-col>
        </el-row>
      </el-header>
      <el-divider></el-divider>
      <el-main
        style="width: 100%; text-align: left; padding: 0px 20px 20px 30px;"
      >
        <h4>가입한 스터디</h4>
        <div>
          <el-scrollbar height="100px" always>
            <li v-for="std in studyData.joined_study" :key="std">
              {{ std }}
            </li>
          </el-scrollbar>
        </div>
      </el-main>
      <el-footer>
        <div>
          <el-rate
            style="height: 100px; display: inline-block; margin-right: 10px"
            :icon-classes="state.iconClasses"
            v-model="state.rating"
          ></el-rate>
          <el-button
            v-if="studyData.checked == false"
            style="display: inline-block"
            @click="handleEvaluate"
            plain
            type="success"
            >평가</el-button
          >
          <el-button v-else style="display: inline-block">평가완료</el-button>
        </div>
      </el-footer>
    </el-container>
  </div>
</template>

<script>
import { onMounted, reactive, ref, watch } from "vue";
import { useStore } from "vuex";
import { ElMessage } from "element-plus";
import axios from "axios";

export default {
  name: "evaluate-detail",

  props: {
    memberData: {
      type: Object
    },
    studyData: {
      type: Object
    }
  },
  setup(props, { emit }) {
    const store = useStore();

    const state = reactive({
      formLabelWidth: "120px",
      rating: 0,
      passion: 50,
      iconClasses: ["el-icon-star-on", "el-icon-star-on", "el-icon-star-on"],
      circleUrl:
        "https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png",
      uri: "",
      studyData: ""
    });

    watch(
      () => props.memberData,
      () => {
        state.studyData = props.memberData.profile_thumbnail;
        console.log(state.studyData);
        var name;
        if (!state.studyData) {
          state.studyData = "default.png/default.png/default.png";
        }
        if (
          state.studyData.split("\\").length > state.studyData.split("/").length
        ) {
          name = state.studyData.split("\\");
        } else {
          name = state.studyData.split("/");
        }
        console.log(name);
        axios({
          url: `https://i5b306.p.ssafy.io/api/v1/users/profile/${name[2]}/download`,
          method: "GET",
          responseType: "blob"
        }).then(res => {
          state.uri = URL.createObjectURL(res.data);
        });
      }
    );

    onMounted(() => {
      state.studyData = props.memberData.profile_thumbnail;
      console.log(state.studyData);
      var name;
      if (!state.studyData) {
        state.studyData = "default.png/default.png/default.png";
      }
      if (
        state.studyData.split("\\").length > state.studyData.split("/").length
      ) {
        name = state.studyData.split("\\");
      } else {
        name = state.studyData.split("/");
      }
      console.log(name);
      axios({
        url: `https://i5b306.p.ssafy.io/api/v1/users/profile/${name[2]}/download`,
        method: "GET",
        responseType: "blob"
      }).then(res => {
        state.uri = URL.createObjectURL(res.data);
      });
    });

    const handleEvaluate = function() {
      let body = new FormData();
      body.append("rating", state.rating);
      body.append("studyId", props.studyData["study_id"]);
      body.append("studyMemberId", props.studyData["studyMemberId"]);

      store
        .dispatch("root/requestSendMemberRating", body)
        .then(function(res) {
          console.log(res);
          ElMessage({
            message: "평가완료",
            type: "success"
          });
          store.commit("root/setIsevaluate", false);
          store.commit("root/setDeleteEvaluatedMember", {
            studyId: props.studyData["study_id"],
            studyMemberId: props.studyData["studyMemberId"]
          });
        })
        .catch(function(err) {
          console.log(err);
        });
    };

    return { state, handleEvaluate };
  }
};
</script>

<style>
.evaluate-box {
  height: 500px;
  border: solid;
}
.el-rate__icon {
  font-size: 2.5rem;
}
.study_joined {
  display: flex;
  justify-content: flex-start;
}
</style>
