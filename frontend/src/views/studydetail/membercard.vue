<template>
  <el-card :body-style="{ padding: '0px' }">
    <div class="image-wrapper">
      <el-image style="width: 100%; height: 200px"
        :src="'https://localhost:8443/' + memberData.thumbnail"
        :fit="fit">
      </el-image>
    </div>
    <div style="text-align: left; padding: 14px;">
      <span>{{memberData.name}}님</span>
    </div>
  </el-card>
</template>

<style scoped>
.el-card {
  width: 300px;
  height: auto;
  margin: 0 8px;
  margin-bottom: 40px;
}
.el-card .image-wrapper {
  width: 100%;
  height: 200px;
}

</style>

<script>
import { onMounted, reactive } from 'vue'
import { useStore } from 'vuex'

export default {
  name: 'membercard',
  props: {
    memberData: {
      type: Object
    }
  },

  setup (props, {emit} ) {
    const store = useStore()
    const state = reactive({

   })

    // 페이지 진입시 불리는 훅
    onMounted (() => {
    })



  return { state }
  }
}
</script>
