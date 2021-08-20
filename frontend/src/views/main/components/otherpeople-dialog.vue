<template>
  <el-dialog
    custom-class="otherpeople-dialog"
    title="프로필"
    v-model="state.dialogVisible"
    @close="handleClose"
  >
    <el-row :gutter="24">
      <el-col :span="10">
        <div style="display: inline-block">
          <el-progress
            type="dashboard"
            :percentage="userData.temperature"
            :color="state.colors"
            :width="180"
            :stroke-width="7"
            status="success"
          >
            <el-avatar :src="state.uri" :fit="fill" :size="150"></el-avatar>
          </el-progress>
        </div>
      </el-col>
      <el-col :span="12">
        <p style="font-size: 30px; font-weight: bold; margin: 5px">
          {{ userData.name }}
        </p>
        <p style="font-size: 20px; font-weight: bold; margin-left: 5px">
          열정도: {{ userData.temperature }}°C
        </p>
      </el-col>
    </el-row>
    <el-divider style="margin: 5px;"></el-divider>
    <el-row>
      <h3>가입한 스터디({{ studyData.length }}개)</h3>
      <el-col>
        <div>
          <el-scrollbar height="80px" always>
            <li v-for="std in studyData" :key="std">
              {{ std }}
            </li>
          </el-scrollbar>
        </div>
      </el-col>
    </el-row>
    <template #footer>
      <span>관심분야: </span>
      <span v-for="interest in userData.interest" :key="interest"
        >#{{ interest }}
      </span>
    </template>
  </el-dialog>
</template>
<style>
.otherpeople-dialog {
  width: 500px !important;
  height: 500px;
}
.otherpeople-dialog .el-dialog__headerbtn {
  float: right;
}
.otherpeople-dialog .el-dialog__body {
  height: 330px;
}
.otherpeople-dialog .el-dialog__footer {
  border: solid;
  text-align: left;
  padding: 10px;
  margin-left: 20px;
  margin-right: 20px;
}
</style>
<script>
import { reactive, computed, ref, watch } from "vue";
import { useStore } from "vuex";
import { useRouter } from "vue-router";
import axios from "axios";
export default {
  name: "otherpeople-dialog",

  props: {
    open: {
      type: Boolean,
      default: false
    },
    userData: {
      type: Object
    },
    studyData: {
      type: Object
    }
  },

  setup(props, { emit }) {
    const store = useStore();
    const otherpeopleForm = ref(null);
    const router = useRouter();

    const state = reactive({
      dialogVisible: computed(() => props.open),
      formLabelWidth: "120px",
      passion: 50,
      colors: [
        { color: "#f56c6c", percentage: 20 },
        { color: "#e6a23c", percentage: 30 },
        { color: "#5cb87a", percentage: 40 },
        { color: "#1989fa", percentage: 50 },
        { color: "#6f7ad3", percentage: 70 }
      ],
      uri: "",
      studyData: ""
    });

    watch(
      () => props.userData,
      () => {
        state.studyData = props.userData.profile_thumbnail;
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
        axios({
          url: `https://i5b306.p.ssafy.io/api/v1/users/profile/${name[2]}/download`,
          method: "GET",
          responseType: "blob"
        }).then(res => {
          state.uri = URL.createObjectURL(res.data);
        });
      }
    );

    const handleClose = function() {
      emit("closeOtherpeopleDialog");
    };

    return { otherpeopleForm, state, handleClose };
  }
};
</script>
