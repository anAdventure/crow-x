<template>
  <div class="app-container">
    <el-form
      :model="queryParams"
      ref="queryForm"
      :inline="true"
      v-show="showSearch"
      label-width="68px"
    >
      <el-form-item label="车牌号" prop="carNumber">
        <el-input
          v-model="queryParams.carNumber"
          placeholder="请输入车牌号"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>

      <el-form-item>
        <el-button
          type="primary"
          icon="el-icon-search"
          size="mini"
          @click="handleQuery"
          >搜索</el-button
        >
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery"
          >重置</el-button
        >
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['system:trailer:add']"
          >新增</el-button
        >
      </el-col>

      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          :loading="exportLoading"
          @click="handleExport"
          v-hasPermi="['system:trailer:export']"
          >导出</el-button
        >
      </el-col>
      <right-toolbar
        :showSearch.sync="showSearch"
        @queryTable="getList"
      ></right-toolbar>
    </el-row>

    <el-table
      v-loading="loading"
      :data="trailerList"
      @selection-change="handleSelectionChange"
    >
      <!-- <el-table-column type="selection" width="65" align="center" /> -->
      <el-table-column label="编号" align="center" prop="id" />
      <el-table-column label="毛重" align="center" prop="grossWeight" />
      <el-table-column label="皮重" align="center" prop="tareWeight" />
      <el-table-column label="净重" align="center" prop="netWeight" />
      <el-table-column label="单价" align="center" prop="price" />
      <el-table-column label="车牌号" align="center" prop="carNumber" />
      <el-table-column label="总价格" align="center" prop="allPrice" />
      <el-table-column
        label="创建时间"
        align="center"
        prop="createTime"
        width="200"
      />
      <el-table-column
        label="操作"
        align="center"
        class-name="small-padding fixed-width"
      >
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:trailer:edit']"
            >修改</el-button
          >
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:trailer:remove']"
            >删除</el-button
          >
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total > 0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改半挂对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="毛重" prop="grossWeight">
          <el-input
            v-model="form.grossWeight"
            placeholder="请输入毛重"
            @keyup.native="calNetWeight"
          />
        </el-form-item>
        <el-form-item label="皮重" prop="tareWeight">
          <el-input
            v-model="form.tareWeight"
            placeholder="请输入皮重"
            @keyup.native="calNetWeight"
          />
        </el-form-item>
        <el-form-item label="净重" prop="netWeight" disabled>
          <el-input
            :disabled="true"
            v-model="form.netWeight"
            placeholder="请输入净重"
          />
        </el-form-item>
        <el-form-item label="单价" prop="price">
          <el-input
            v-model="form.price"
            placeholder="请输入单价"
            @keyup.native="calPrice"
          />
        </el-form-item>
        <el-form-item label="车牌号" prop="carNumber">
          <el-input v-model="form.carNumber" placeholder="请输入车牌号" />
        </el-form-item>
        <el-form-item label="总价格" prop="allPrice">
          <el-input v-model="form.allPrice" placeholder="请输入总价格" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  listTrailer,
  getTrailer,
  delTrailer,
  addTrailer,
  updateTrailer,
  exportTrailer,
} from "@/api/system/trailer";

export default {
  name: "Trailer",
  data() {
    return {
      // 遮罩层
      loading: true,
      // 导出遮罩层
      exportLoading: false,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 半挂表格数据
      trailerList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        grossWeight: null,
        tareWeight: null,
        netWeight: null,
        price: null,
        carNumber: null,
        allPrice: null,
      },
      // 表单参数
      form: {
        grossWeight: 0,
        tareWeight: 0,
        netWeight: 0,
      },
      // 表单校验
      rules: {},
    };
  },

  created() {
    this.getList();
  },
  watch: {
    //  watchCalNetWeight(){
    //   this.form.netWeight =  (this.form.grossWeight)  - ( this.form.tareWeight)
    // },
  },
  methods: {
    calNetWeight() {
      console.log("计算价格");
      this.form.netWeight = this.calc(
        this.form.grossWeight,
        this.form.tareWeight,
        "-"
      );
      this.calPrice();
    },
    calPrice() {
      console.log("计算价格");
      this.form.allPrice = this.calc(this.form.price, this.form.netWeight, "*");
    },
    /** 查询半挂列表 */
    getList() {
      this.loading = true;
      listTrailer(this.queryParams).then((response) => {
        this.trailerList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        id: null,
        grossWeight: null,
        tareWeight: null,
        netWeight: null,
        price: null,
        carNumber: null,
        allPrice: null,
        createTime: null,
        updateTime: null,
        createBy: null,
        updateBy: null,
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map((item) => item.id);
      this.single = selection.length !== 1;
      this.multiple = !selection.length;
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加半挂";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids;
      getTrailer(id).then((response) => {
        this.form = response.data;
        this.open = true;
        this.title = "修改半挂";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate((valid) => {
        if (valid) {
          if (this.form.id != null) {
            updateTrailer(this.form).then((response) => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addTrailer(this.form).then((response) => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$modal
        .confirm('是否确认删除半挂编号为"' + ids + '"的数据项？')
        .then(function () {
          return delTrailer(ids);
        })
        .then(() => {
          this.getList();
          this.$modal.msgSuccess("删除成功");
        })
        .catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$modal
        .confirm("是否确认导出所有半挂数据项？")
        .then(() => {
          this.exportLoading = true;
          return exportTrailer(queryParams);
        })
        .then((response) => {
          this.$download.name(response.msg);
          this.exportLoading = false;
        })
        .catch(() => {});
    },
    calc(num1, num2, calcStr) {
      num1 = num1 || 0;
      num2 = num2 || 0;
      var str1, // 转换为字符串的数字
        str2,
        ws1 = 0, // ws1，ws2 用来存储传入的num的小数点后的数字的位数
        ws2 = 0, // 赋默认值，解决当整数和小数运算时倍数计算错误导致的结果误差
        bigger, // bigger和smaller用于加，减，除法找出小的那个数字，给后面补0，解决位数不对从而造成的计算错误的问题；乘法需要将结果除两个数字的倍数之和
        smaller, // 例如：加减除法中1.001 + 2.03 ，如果不给2.03进行补0，最后会变成1001+203，数字错位导致结果错误；乘法中1.12*1.1会放大为112*11，所以结果需要除以1000才会是正确的结果，112*11/1000=1.232
        zeroCount, // 需要补充0的个数
        isExistDot1, // 传入的数字是否存在小数点
        isExistDot2,
        sum,
        beishu = 1;
      // 将数字转换为字符串
      str1 = num1.toString();
      str2 = num2.toString();
      // 是否存在小数点（判断需要计算的数字是不是包含小数）
      isExistDot1 = str1.indexOf(".") != -1 ? true : false;
      isExistDot2 = str2.indexOf(".") != -1 ? true : false;
      // 取小数点后面的位数
      if (isExistDot1) {
        ws1 = str1.split(".")[1].length;
      }

      if (isExistDot2) {
        ws2 = str2.split(".")[1].length;
      }
      // 如ws1 和 ws2 无默认值，如果num1 或 num2 不是小数的话则 ws1 或 ws2 的值将为 undefined
      // bigger 和 smaller 的值会和预期不符
      bigger = ws1 > ws2 ? ws1 : ws2;
      smaller = ws1 < ws2 ? ws1 : ws2;

      switch (calcStr) {
        // 加减法找出小的那个数字，给后面补0，解决位数不对从而造成的计算错误的问题
        // 例如：1.001 + 2.03 ，如果不给2.03进行补0，最后会变成1001+203，数字错位导致结果错误
        case "+":
        case "-":
        case "/":
          zeroCount = bigger - smaller;
          for (var i = 0; i < zeroCount; i++) {
            if (ws1 == smaller) {
              str1 += "0";
            } else {
              str2 += "0";
            }
          }
          break;
        case "*":
          // 乘法需要将结果除两个数字的倍数之和
          bigger = bigger + smaller;
          break;
        default:
          return "暂不支持的计算类型，现已支持的有加法、减法、乘法、除法";
          break;
      }

      // 去除数字中的小数点
      str1 = str1.replace(".", "");
      str2 = str2.replace(".", "");

      // 计算倍数，例如：1.001小数点后有三位，则需要乘 1000 变成 1001，变成整数后精度丢失问题则不会存在
      for (var i = 0; i < bigger; i++) {
        beishu *= 10; // 等价于beishu = beishu * 10;
      }
      num1 = parseInt(str1);
      num2 = parseInt(str2);
      // 进行最终计算并除相应倍数
      switch (calcStr) {
        case "+":
          sum = (num1 + num2) / beishu;
          break;
        case "-":
          sum = (num1 - num2) / beishu;
          break;
        case "*":
          sum = (num1 * num2) / beishu;
          break;
        case "/":
          sum = num1 / num2;
          /* 除数与被除数同时放大一定倍数，不影响结果，
			所以对数字进行放大对应倍数并进行补0操作后不用另对倍数做处理 */
          break;
        default:
          return "暂不支持的计算类型，现已支持的有加法、减法、乘法、除法";
      }

      return sum;
    },
  },
};
</script>
