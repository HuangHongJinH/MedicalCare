<template>
  <el-container style="height: 100vh;width:150vh">
    <!-- 左侧原始图片显示区 -->
    <el-aside width="40%" style="background-color: rgb(238, 241, 246)">
      <el-image fit="fill" :src="originalImage" alt="原始图片" class="image"></el-image>
    </el-aside>
    <!-- 中间编码后的图片显示区 -->
    <el-aside width="40%" style="background-color: rgb(255, 255, 255);">
      <el-image fit="fill" :src="encodedImage" alt="编码后的图片" class="image"></el-image>
    </el-aside>

    <!-- 右侧功能区 -->
    <el-aside width="auto" style="background-color: rgb(238, 241, 246);">
      <el-upload class="upload-demo" action="#" :before-upload="handleUpload" :auto-upload="true" :show-file-list="false">
        <el-button slot="trigger" size="small" type="primary">选择文件</el-button>
      </el-upload>
      <el-select v-model="imageType" placeholder="请选择图片类型">
        <el-option label="明文图" value="original"></el-option>
        <el-option label="密文图" value="encoded"></el-option>
      </el-select>
      <el-button type="success" @click="encryptImage">加密</el-button>
      <el-button type="danger" @click="decryptImage">解密</el-button>
        <el-button type="danger" @click="requestAuthorization">向android发送授权</el-button>
    </el-aside>
  </el-container>
</template>

<script>
export default {
  data() {
    return {
      originalImage: "",
      encodedImage: '',
      imageType: 'original'
    };
  },
  methods: {
    mounted() {
      window.addEventListener('message', (event) => {
        try {
          const data = JSON.parse(event.data);
          if (data.type === 'AUTH_RESPONSE' && data.authorized === true) {
            // 若安卓发送授权允许，则触发事件
            this.onAuthorizationGranted();
          }
        } catch (error) {
          console.error('Error parsing message from Android:', error);
        }
      });
    },
    requestAuthorization() {
      if (window.Android) {
        // 发送Webview异步授权通知给安卓
          alert("已发送请求！")
        window.Android.requestAuthorization(JSON.stringify({ type: 'AUTH_REQUEST' }));
      }
    },
    // 安卓授权允许后触发的事件
    onAuthorizationGranted() {
      alert('授权已被允许');
    },
    handleUpload(file) {
      const isImage = file.type.startsWith('image/');
      if (!isImage) {
        this.$message.error('请上传图片文件。');
        return false;
      }
      const reader = new FileReader();
      reader.onload = (e) => {
        if (this.imageType === 'original') {
          this.originalImage = e.target.result;
        } else if (this.imageType === 'encoded') {
          this.encodedImage = e.target.result;
        }
      };
      reader.readAsDataURL(file);
      return true;
    },
    encryptImage() {
      // 处理加密逻辑
    },
    decryptImage() {
      // 处理解密逻辑
    }
  }
};
</script>

<style>
.image {
  max-width: 100%;
  height: auto;
}
</style>
