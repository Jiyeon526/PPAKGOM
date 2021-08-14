<template>
  <el-dialog
    custom-class="otherpeople-dialog"
    title="프로필"
    v-model="state.dialogVisible"
    @close="handleClose"
  >
    <el-row :gutter="24">
      <el-col :span="16">
        <p>{{ userData.name }}</p>
        <p>{{ state.passion }}</p>
        <el-progress
          :stroke-width="20"
          :percentage="state.passion"
          :show-text="false"
        >
        </el-progress>
      </el-col>
      <el-col :span="8">
        <el-image
          style="width: 100px; height: 100px"
          :src="'https://i5b306.p.ssafy.io/image/study/default.png'"
          :fit="fit"
        >
        </el-image>
      </el-col>
    </el-row>
    <el-divider></el-divider>
    <el-row>
      <el-col>
        <p>가입된 스터디</p>
        <li v-for="std in userData.joined_study">
          {{ std }}
        </li>
      </el-col>
    </el-row>
    <template #footer>
      <span>관심분야: </span>
      <span v-for="interest in userData.interest">#{{ interest }} </span>
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
import { reactive, computed, ref, onMounted } from "vue";
import { useStore } from "vuex";
import { useRouter } from "vue-router";
import { ElMessage } from "element-plus";
export default {
  name: "otherpeople-dialog",

  props: {
    open: {
      type: Boolean,
      default: false
    },
    userData: {
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
      circleUrl:
        "https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png"
    });

    onMounted(() => {
      console.log("1235", props.userData);
      // console.log(otherpeopleForm.value)
    });

    const handleClose = function() {
      emit("closeOtherpeopleDialog");
    };

    return { otherpeopleForm, state, handleClose };
  }
};
</script>
