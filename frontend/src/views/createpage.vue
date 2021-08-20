<template>
  <el-container>
    <el-header>
      <h2>스터디 생성</h2>
    </el-header>
    <el-main class="room-create-main">
      <span class="room-create-main-block">
        <el-form
          :model="state.form"
          :rules="state.rules"
          ref="roomForm"
          style="display:block"
        >
          <el-form-item
            label="제목"
            prop="title"
            :label-width="state.formLabelWidth"
          >
            <el-input v-model="state.form.title" autocomplete="off"></el-input>
          </el-form-item>

          <el-form-item
            label="설명"
            prop="description"
            :label-width="state.formLabelWidth"
          >
            <el-input
              type="textarea"
              :rows="2"
              v-model="state.form.description"
            ></el-input>
          </el-form-item>

          <el-form-item
            label="관심사"
            prop="dynamicTags"
            :label-width="state.formLabelWidth"
          >
            <el-tag
              :key="tag"
              v-for="tag in state.form.dynamicTags"
              closable
              :disable-transitions="false"
              @close="handleTagClose(tag)"
            >
              {{ tag }}
            </el-tag>
            <el-input
              class="input-new-tag"
              v-if="state.inputVisible"
              v-model="state.inputValue"
              ref="saveTagInput"
              size="mini"
              @keyup.enter="handleInputConfirm"
              @blur="handleInputConfirm"
            >
            </el-input>
            <el-button
              v-else
              class="button-new-tag"
              size="small"
              @click="showInput"
              >+ New Tag</el-button
            >
          </el-form-item>

          <el-form-item
            label="필요 최소온도"
            prop="passion"
            :label-width="state.formLabelWidth"
          >
            <el-slider
              class="slider-reverse"
              v-model="state.form.passion"
              max="100"
              step="1"
              :marks="state.marks"
            >
            </el-slider>
            <div>
              <el-progress
                :text-inside="true"
                :stroke-width="20"
                :percentage="state.form.passion"
                status="exception"
                style="margin:5px;"
              >
                <span>필요 열정도</span>
              </el-progress>
              <el-input-number
                v-model="state.form.passion"
                :precision="2"
                :step="0.5"
                :max="100"
              ></el-input-number>
            </div>
          </el-form-item>

          <el-form-item
            label="인원수"
            prop="num"
            :label-width="state.formLabelWidth"
          >
            <el-input-number
              v-model="state.form.num"
              controls-position="right"
              @change="numChange"
              :min="1"
              :max="10"
            ></el-input-number>
          </el-form-item>

          <el-form-item
            label="모집 마감 날짜"
            prop="deadline"
            :label-width="state.formLabelWidth"
          >
            <el-date-picker
              v-model="state.form.deadline"
              type="date"
              placeholder="마감날짜를 정하세요"
              format="YYYY-MM-DD"
              value-format="YYYY-MM-DD"
            >
            </el-date-picker>
          </el-form-item>

          <el-form-item
            label="썸네일"
            prop="thumbnail"
            :label-width="state.formLabelWidth"
          >
            <el-upload
              class="upload-demo"
              action="https://jsonplaceholder.typicode.com/posts/"
              accept=".png, .jpg, .jpeg, .gif"
              list-type="picture"
              :on-change="prevUpload"
              :auto-upload="false"
              thumbnail-mode="true"
              ref="toUpload"
              :on-remove="handleRemove"
            >
              <i class="el-icon-upload"></i>
              <div>
                스터디 썸네일을 올려주세요!
                <i class="el-icon-mouse">(Click)</i>
              </div>
              <template #tip> </template>
            </el-upload>
          </el-form-item>
        </el-form>
      </span>
    </el-main>

    <el-footer>
      <el-button
        style="width: 30%"
        type="success"
        plain
        @click="clickCreateRoom"
        >스터디 생성하기</el-button
      >
    </el-footer>
  </el-container>
</template>

<script>
import { computed, reactive, ref, onMounted, nextTick } from "vue";
import { useStore } from "vuex";
import { ElMessage } from "element-plus";
import { useRouter } from "vue-router";
import ex4 from "@/assets/images/ex4.jpg";

export default {
  name: "Createpage",

  setup(props, { emit }) {
    const store = useStore();
    const router = useRouter();
    const roomForm = ref(null);
    const toUpload = ref(null);
    const saveTagInput = ref(null);

    const state = reactive({
      form: {
        title: "",
        description: "",
        dynamicTags: [],
        passion: 36.5,
        num: 1,
        deadline: "",
        thumbnail: []
      },
      rules: {
        title: [
          { required: true, message: "필수 입력 항목입니다.", trigger: "blur" },
          {
            validator(rule, value) {
              var error = [];
              if (value.length > 30) {
                error = ["최대 30자까지 입력가능합니다."];
              }
              return error;
            }
          }
        ],
        description: [
          { required: true, message: "필수 입력 항목입니다.", trigger: "blur" }
        ],
        thumbnail: [
          {
            type: "array",
            required: true,
            message: "필수 입력 항목입니다.",
            trigger: "blur"
          },
          {
            validator(rule, value) {
              const file_size = value[1];
              const file_name = value[0];
              const file_length = file_name.length;
              const dot = file_name.lastIndexOf(".");
              const file_extension = file_name
                .substring(dot, file_length)
                .toLowerCase();
              const extension_only = [".png", ".jpg", ".jpeg", ".gif"];
              var error = [];
              if (file_size > 1024 * 1025) {
                error = ["1GB 이하만 가능합니다."];
              } else if (!extension_only.includes(file_extension)) {
                error = [
                  "업로드 가능한 파일의 확장자는 png, jpg, jpeg, gif 입니다."
                ];
              }
              return error;
            }
          }
        ]
      },
      uploading: [],
      marks: {
        36.5: "36.5°C"
      },
      inputVisible: false,
      inputValue: "",
      formLabelWidth: "100px",
      fileList: [
        {
          name: "default.jpg",
          url: ex4
        }
      ]
    });

    onMounted(() => {
      store.commit("root/setMenuActiveMenuName", "create");
    });

    const prevUpload = function(file) {
      const necessary = [];
      necessary.push(file["name"]);
      necessary.push(file["size"]);
      state.form.thumbnail = necessary;
      console.log(state.form.passion);
      state.uploading = file.raw;
    }

    const clickCreateRoom = function() {
      roomForm.value.validate(valid => {
        if (valid) {
          let body = new FormData();
          body.append("name", state.form.title);
          body.append("content", state.form.description);
          body.append("temperature", state.form.passion);
          body.append("interest", state.form.dynamicTags);
          body.append("deadline", state.form.deadline);
          body.append("population", state.form.num);
          body.append("study_thumbnail", state.uploading);
          store
            .dispatch("root/requestCreateRoom", body)
            .then(function(result) {
              toUpload.value.submit();
              ElMessage({
                message: "새 방이 생성되었습니다.",
                type: "success"
              });
              handleClose();
            })
            .catch(function(err) {
              alert(err.message);
            });
        } else {
          alert("Validate error!");
        }
      });
    };

    const handleClose = function() {
      state.form.title = ""
      state.form.description = ""
      state.form.thumbnail = []
      state.form.dynamicTags = []
      state.form.passion = 36.5
      state.form.num = 1
      state.form.deadline = ""
      router.push({ name: "home" })
    };

    const handleTagClose = function(tag) {
      state.form.dynamicTags.splice(state.form.dynamicTags.indexOf(tag), 1);
    };

    const showInput = function() {
      state.inputVisible = true;
      nextTick(() => {
        saveTagInput.value.input.focus();
      });
    };

    const handleInputConfirm = function() {
      let inputValue = state.inputValue;
      if (inputValue) {
        state.form.dynamicTags.push(state.inputValue);
      }
      state.inputVisible = false;
      state.inputValue = "";
    };

    const handleRemove = function(file, fileList) {
    };

    return {
      roomForm,
      toUpload,
      saveTagInput,
      state,
      handleClose,
      clickCreateRoom,
      prevUpload,
      handleTagClose,
      showInput,
      handleInputConfirm,
      handleRemove
    };
  }
};
</script>

<style>
.el-tag + .el-tag {
  margin-left: 10px;
}
.button-new-tag {
  margin-left: 10px;
  height: 32px;
  line-height: 30px;
  padding-top: 0;
  padding-bottom: 0;
}
.input-new-tag {
  width: 90px;
  margin-left: 10px;
  vertical-align: bottom;
}
.slider-reverse .el-slider__runway {
  width: 100%;
  height: var(--el-slider-height);
  margin: var(--el-slider-margin);
  /* background-color: var(--el-slider-main-background-color); */

  border-radius: var(--el-slider-border-radius);
  position: relative;
  cursor: pointer;
  vertical-align: middle;
  color: lawngreen;
}
.slider-reverse .el-slider__bar {
  height: var(--el-slider-height);
  /* background-color: var(--el-slider-runway-background-color); */
  background-color: seagreen;
  border-top-left-radius: var(--el-slider-border-radius);
  border-bottom-left-radius: var(--el-slider-border-radius);
  color: lawngreen;
  position: absolute;
}

.room-create-main {
  width: 100%;
  justify-content: center;
  text-align: center;
  display: inline-block;
}

.room-create-main-block {
  width: 75%;
  justify-content: center;
  text-align: center;
  display: inline-block;
}
</style>
