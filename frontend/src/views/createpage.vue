<template>
    <el-container>
      <el-header>
        스터디 생성
      </el-header>

      <el-main>
        <el-form
          :model="state.form"
          :rules="state.rules"
          ref="roomForm"
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
            <el-input type="textarea" :rows=2 v-model="state.form.description"></el-input>
          </el-form-item>

          <el-form-item
            label="분야"
            prop="field"
            :label-width="state.formLabelWidth"
          >
            <el-input v-model="state.form.field" autocomplete="off"></el-input>
          </el-form-item>

          <el-form-item
            label="열정도"
            prop="passion"
            :label-width="state.formLabelWidth"
          >
            <el-input v-model="state.form.passion" autocomplete="off"></el-input>
          </el-form-item>

          <el-form-item
            label="인원수"
            prop="num"
            :label-width="state.formLabelWidth"
          >
            <el-input-number v-model="state.form.num" controls-position="right" @change="numChange" :min="1" :max="10"></el-input-number>
          </el-form-item>

          <el-form-item
            label="모집 날짜"
            prop="deadline"
            :label-width="state.formLabelWidth"
          >
            <el-date-picker
              v-model="state.form.deadline"
              type="daterange"
              start-placeholder="Start Date"
              end-placeholder="End Date">
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
              thumbnail-mode=true
              ref="toUpload"
            >
              <i class="el-icon-plus"></i>
            </el-upload>
          </el-form-item>
        </el-form>
      </el-main>

      <el-footer>
        <el-button style="width: 30%" type="primary" @click="clickCreateRoom">생성</el-button>
      </el-footer>

    </el-container>
</template>

<script>
import { computed, reactive, ref, onMounted } from "vue"
import { useStore } from "vuex";
import { ElMessage } from "element-plus";

export default {
  name: 'Createpage',

  setup(props, { emit }) {
    const store = useStore()
    const roomForm = ref(null)
    const toUpload = ref(null)

    const state = reactive({
      form: {
        title: '',
        description: '',
        field: '',
        passion: '',
        num: 1,
        deadline: '',
        thumbnail: [],
      },
      rules: {
        title: [
          { required: true, message: "필수 입력 항목입니다.", trigger: "blur" },
          {
            validator(rule,value) {
              var error = []
              if (value.length > 30) {
                error = ["최대 30자까지 입력가능합니다."]
              }
              return error
            }
          }
        ],
        description: [{ required: true, message: "필수 입력 항목입니다.", trigger: "blur" }],
        thumbnail: [
          { type: 'array', required: true, message: "필수 입력 항목입니다.", trigger: "blur" },
          {
            validator(rule,value) {
              console.log("11",value)
              const file_size = value[1]
              const file_name = value[0]
              const file_length = file_name.length
              console.log(file_name,file_length)
              const dot = file_name.lastIndexOf('.')
              const file_extension = file_name.substring(dot,file_length).toLowerCase()
              const extension_only = ['.png', '.jpg', '.jpeg', '.gif']
              console.log(file_extension)
              var error = []
              if (file_size > 1024*500) {
                error = ["500KB 이하만 가능합니다."]
                console.log(error)
              } else if (!extension_only.includes(file_extension)) {
                error = ["업로드 가능한 파일의 확장자는 png, jpg, jpeg, gif 입니다."]
                console.log(error)
              }
              return error
            }
          },
        ],
      },
      uploading: [],
      formLabelWidth: "100px",
    })

    onMounted(() => {
      store.commit("root/setMenuActiveMenuName", "create");
      // console.log(toUpload.value)
    });

    const prevUpload = function(file) {
      const necessary = []
      necessary.push(file['name'])
      necessary.push(file['size'])
      state.form.thumbnail = necessary

      state.uploading = file.raw
      console.log('111', file, state.form.thumbnail, typeof state.form.thumbnail)
    }

    const clickCreateRoom = function() {

      console.log("check",state.uploading)
      // roomForm.value.validate(valid => {
      //   console.log("!!!",valid)
      //   if (valid) {
      //     console.log("submit");
      //     let body = new FormData();
      //     body.append("title", state.form.title);
      //     body.append("description", state.form.description);
      //     body.append("thumbnail", state.uploading);
      //     console.log("여기까지 확인!")
      //     store
      //       .dispatch("root/requestCreateRoom", body)
      //       .then(function(result) {
      //         console.log(result)
      //         toUpload.value.submit()
      //         ElMessage({
      //           message: "새 방이 생성되었습니다.",
      //           type: "success"
      //         });
      //         handleClose();
      //       })
      //       .catch(function(err) {
      //         alert(err.message);
      //       });
      //   } else {
      //     alert("Validate error!");
      //   }
      // })
    }

    const handleClose = function() {
      state.form.title = "";
      state.form.description = "";
      state.form.thumbnail = [],
      emit("closeRoomDialog")
    }

    const numChange = function() {
      // console.log(state.form.num)
    }

    return { roomForm, toUpload, state, handleClose, clickCreateRoom, prevUpload, numChange }
  }
}
</script>

<style>

</style>
