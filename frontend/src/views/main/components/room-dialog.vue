<template>
  <el-dialog
    custom-class="room-dialog"
    title="컨퍼런스 생성하기"
    v-model="state.dialogVisible"
    @close="handleClose"
  >
    <el-form
      :model="state.form"
      :rules="state.rules"
      ref="roomForm"
    >
      <el-form-item
        label="용도"
        prop="category"
        :label-width="state.formLabelWidth"
      >
        <el-select class="select-box" v-model="state.value">
          <el-option
            v-for="item in state.options"
            :key="item.value"
            :label="item.label"
            :value="item.value">
          </el-option>
        </el-select>
      </el-form-item>

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
        label="썸네일"
        prop="thumbnail"
        :label-width="state.formLabelWidth"
      >

        <el-upload
          class="upload-demo"
          action="https://jsonplaceholder.typicode.com/posts/"
          accept=".png, .jpg, .jpeg, .gif"
          list-type="picture"
          :before-upload="prevUpload"
          :auto-upload="false"
          thumbnail-mode=true
          ref="toUpload"
        >
          <i class="el-icon-plus"></i>
        </el-upload>

      </el-form-item>
    </el-form>

    <template #footer>
      <span class="dialog-footer">
        <el-button type="primary" @click="clickCreateRoom">생성</el-button>
      </span>
    </template>

  </el-dialog>
</template>

<script>
import { computed, reactive, ref, onMounted } from "vue"
import { useStore } from "vuex";
import { ElMessage } from "element-plus";

export default {
  name: 'room-dialog',

  props: {
    open: {
      type: Boolean,
      default: false
    }
  },

  setup(props, { emit }) {
    const store = useStore()
    const roomForm = ref(null)
    const toUpload = ref(null)

    const state = reactive({
      form: {
        title: '',
        description: '',
        category: 1,
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
              // const file_size = value.size
              // const file_name = value.name
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
      options: [{
        value: 1,
        label: '업무'
      }, {
        value: 2,
        label: '교육'
      }, {
        value: 3,
        label: '기타'
      }],
      value: 1,
      uploading: [],
      dialogVisible: computed(() => props.open),
      formLabelWidth: "100px",
    })

    onMounted(() => {
      // console.log(roomForm.value)
      // console.log(toUpload.value)
    });

    // 썸네일 데이터 가져오기
    const prevUpload = function(file) {
      const necessary = []
      necessary.push(file['name'])
      necessary.push(file['size'])
      state.form.thumbnail = necessary

      state.uploading = file
      console.log('111', file, state.form.thumbnail, typeof state.form.thumbnail)
    }

    // 유효성 검사 & 방만들기
    const clickCreateRoom = function() {
      // 썸네일 유효성 검사 위한 submit
      toUpload.value.submit()
      state.form.category = state.value
      // 유효성 검사
      roomForm.value.validate(valid => {
        if (valid) {
          console.log("submit")
          let body = new FormData()
          body.append("title", state.form.title)
          body.append("description", state.form.description)
          body.append("conference_category", state.form.category)
          body.append("thumbnail", state.uploading)
          console.log("여기까지 확인!")
          store
            .dispatch("root/requestCreateRoom", body)
            .then(function(result) {
              console.log(result)
              ElMessage({
                message: "새 방이 생성되었습니다.",
                type: "success"
              })
              handleClose()
            })
            .catch(function(err) {
              alert(err.message)
            });
        } else {
          alert("Validate error!")
        }
      })
    }

    const handleClose = function() {
      state.form.title = "";
      state.form.description = "";
      state.form.category = 1,
      state.form.thumbnail = [],
      emit("closeRoomDialog")
    }

    return { roomForm, toUpload, state, handleClose, clickCreateRoom, prevUpload }
  }
}
</script>

<style>
.room-dialog {
  width: 400px !important;
  height: 550px;
}
.room-dialog .el-dialog__headerbtn {
  float: right;
}
.room-dialog .el-form-item__content {
  margin-left: 0 !important;
  float: right;
  width: 200px;
  display: inline-block;
}
.room-dialog .el-form-item {
  margin-bottom: 20px;
}
.room-dialog .el-form-item__error {
  font-size: 12px;
  color: red;
}
.room-dialog .el-input__suffix {
  display: none;
}
.room-dialog .el-dialog__footer {
  margin: 0 calc(50% - 80px);
  padding-top: 0;
  display: inline-block;
}
.room-dialog .dialog-footer .el-button {
  width: 120px;
}
.select-box {
  width: 200px;
  height: 30px;
}
</style>
