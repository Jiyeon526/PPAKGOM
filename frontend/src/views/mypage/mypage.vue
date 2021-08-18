<template>
  <el-container>
    <el-form
      :model="state.form"
      :rules="state.rules"
      ref="editForm"
      :label-position="state.form.align"
      :disabled="!state.editMode"
    >
      <el-form-item
        prop="id"
        label="아이디"
        :label-width="state.formLabelWidth"
      >
        <el-input
          v-model="state.form.id"
          autocomplete="off"
          disabled="true"
        ></el-input>
      </el-form-item>

      <el-form-item
        prop="email"
        label="이메일"
        :label-width="state.formLabelWidth"
      >
        <el-input
          v-model="state.form.email"
          autocomplete="off"
          disabled="true"
        ></el-input>
      </el-form-item>

      <el-form-item
        prop="name"
        label="닉네임"
        :label-width="state.formLabelWidth"
      >
        <el-input v-model="state.form.name" autocomplete="off"></el-input>
      </el-form-item>

      <el-form-item
        prop="interest"
        label="관심사항"
        :label-width="state.formLabelWidth"
      >
        <el-input
          v-model="state.form.interest"
          autocomplete="off"
          clearable
        ></el-input>
      </el-form-item>
    <div v-if="!state.editMode">
      <el-form-item
        prop="thumbnail"
        label="프로필 사진"
        :label-width="state.formLabelWidth"
      >
        <!-- <el-image style="width: 100%; height: 200px"
          :src="'https://localhost:8443/' + state.form.thumbnail"
          :fit="fill">
        </el-image> -->
        <el-image style="width: 100%; height: 200px"
          :src="state.uri"
          :fit="fill">
        </el-image>
      </el-form-item>
    </div>
    <!-- <div v-else>
      <el-form-item
        label="프로필 사진"
        prop="changing"
        :label-width="state.formLabelWidth"
      >

        <el-upload
          class="upload-demo"
          action="https://jsonplaceholder.typicode.com/posts/"
          accept=".png, .jpg, .jpeg, .gif"
          list-type="picture"
          :on-change="prevUpload"
          :auto-upload="false"
          thumbnail-mode=true
          :limit="1"
          ref="toUpload"
          :on-remove="handleRemove"
        >
        <el-button type="primary">Upload</el-button>
        </el-upload>
      </el-form-item>
    </div> -->

    </el-form>
  </el-container>
  <!-- <div v-if="!state.editMode">
    <el-button type="primary" @click="state.editMode = !state.editMode"
      >회원정보 수정</el-button
    >
    <el-button class="delete-btn" type="danger" @click="clickDelete"
      >회원 탈퇴</el-button
    >
  </div>
  <div v-else>
    <el-button type="primary" @click="clickUpdate">수정</el-button>
    <el-button @click="state.editMode = !state.editMode">취소</el-button>
  </div> -->
</template>

<style>
section.el-container {
  justify-content: center;
}
</style>

<script>
import { reactive, ref, onBeforeMount, onMounted, watch } from "vue";
import { ElMessage } from "element-plus";
import { useStore } from "vuex";
import { useRouter } from "vue-router";
import axios from "axios";

export default {
  name: "Mypage",
  setup() {
    const store = useStore();
    const router = useRouter();
    const editForm = ref(null);
    const toUpload = ref(null);

    const state = reactive({
      form: {
        id: "",
        email: "",
        name: "",
        interest: "",
        thumbnail: "",
        changing: "",
        align: "left"
      },
      rules: {
        name: [{ validator: validateName, trigger: "blur" }],
        // department: [{ validator: validateDepartment, trigger: "blur" }],
        // position: [{ validator: validatePosition, trigger: "blur" }]
      },
      editMode: false,
      formLabelWidth: "120px",
      uri: "",
      studyData: "",
    });

    // 페이지 진입시 불리는 훅
    watch(()=>state.form.thumbnail,()=>{
      state.studyData = state.form.thumbnail;
      console.log(state.studyData)
      var name;
      if (!state.studyData) {
        state.studyData = "default.png/default.png/default.png"
      }
      console.log(name)
      if (
        state.studyData.split("\\").length > state.studyData.split("/").length
      ) {
        name = state.studyData.split("\\");
      } else {
        name = state.studyData.split("/");
      }

      axios({
        url: `https://localhost:8443/api/v1/users/profile/${name[2]}/download`,
        method: "GET",
        responseType: "blob"
      }).then(res => {
        state.uri = URL.createObjectURL(res.data);
      })
    })


    const validateName = (rule, value, callback) => {
      if (value === "") {
        callback(new Error("Please input the Name"));
      } else if (value.length > 30) {
        callback(new Error("You can enter up to 30 characters"));
      } else {
        callback();
      }
    };

    const validateDepartment = (rule, value, callback) => {
      if (value.length > 30) {
        callback(new Error("You can enter up to 30 characters"));
      } else {
        callback();
      }
    };

    const validatePosition = (rule, value, callback) => {
      if (value.length > 30) {
        callback(new Error("You can enter up to 30 characters"));
      } else {
        callback();
      }
    };

    const getUserInfo = () => {
      store
        .dispatch("root/requestReadMyInfo")
        .then(function(result) {
          console.log(result);
          state.form.id = result.data.user_id;
          state.form.email = result.data.email;
          state.form.name = result.data.name;
          state.form.interest = result.data.interest;
          // 필요한 url부분만 잘라내기
          const origin_url = result.data.profile_thumbnail
          const need_from = origin_url.indexOf('image')
          const url_length = origin_url.length
          state.form.thumbnail = origin_url.substring(need_from,url_length)
        })
        .catch(function(err) {
          ElMessage.error(err)
        })
    }

    const clickUpdate = function() {
      console.log("확인용",state.form.changing,state.form.thumbnail)
      console.log(editForm);
      editForm.value.validate(valid => {
        if (valid) {
          console.log("submit")
          store
            .dispatch("root/requestUpdateMyInfo", {
              name: state.form.name,
              interest: state.form.interest,
              thumbnail: state.form.changing,
            })
            .then(function(res) {
              ElMessage({
                message: "수정이 완료되었습니다.",
                type: "success"
              });
              // toUpload.value.submit();
              state.editMode = !state.editMode;
              state.form.thumbnail = state.form.changing
              console.log("확인용",state.form.changing,state.form.thumbnail)
              state.form.changing = ""
            })
            .catch(function(err) {
              console.log(err);
            });
        } else {
          ElMessage.error("Validate error!");
        }
      });
    };

    const clickDelete = function() {
      store
        .dispatch("root/requestDeleteMyInfo")
        .then(function(result) {
          store.dispatch("root/requestLogout"); // 로그아웃
          store.commit("root/deleteToken");
          router.push({
            name: "home"
          });

          ElMessage({
            message: "회원탈퇴가 정상적으로 완료되었습니다.",
            type: "success"
          });
        })
        .catch(function(err) {
          console.log(err);
        });
    };

    const prevUpload = function(file) {
      const necessary = [];
      necessary.push(file["name"]);
      necessary.push(file["size"]);
      state.form.changing = necessary;

      state.uploading = file;
      console.log(
        "111",
        file,
        state.form.changing,
        typeof state.form.changing
      );
    };
    // 페이지 진입시 불리는 훅
    onBeforeMount(() => {
      getUserInfo();
    });

    return { editForm, toUpload, state, clickUpdate, clickDelete, prevUpload }
  }
};
</script>
