<template>
  <h2>스터디 멤버관리</h2>
</template>

<script>
import { onMounted } from 'vue'
import { useStore } from 'vuex'

export default {
  name: 'studymember',
  setup () {
    const store = useStore()

    // 페이지 진입시 불리는 훅
    onMounted (() => {

    })
  }
}
</script>
