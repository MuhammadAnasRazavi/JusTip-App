package com.example.justip

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.StringRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.justip.ui.theme.JusTipTheme
import java.text.NumberFormat
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.roundToInt

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JusTipTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    JusTipItLayout()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JusTipItLayout(modifier: Modifier = Modifier) {

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.app_name)
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(121, 74, 250),
                    titleContentColor = Color.White,
                )
            )
        },
        bottomBar = {
            BottomAppBar(
                containerColor = Color(121, 74, 250),
                contentColor = Color.White,
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    text = stringResource(R.string.app_developer_name),
                )
            }
        }
    ) {
        TipCalculatorLayout(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        )
    }
}

@Composable
fun TipCalculatorLayout(modifier: Modifier = Modifier) {

    var amountInput by remember {
        mutableStateOf("")
    }

    val amount = amountInput.toDoubleOrNull() ?: 0.0

    var tipPercentInput by remember {
        mutableStateOf(15f)
    }

    val tipPercent = (tipPercentInput.roundToInt()).toDouble()

    var splitInput by remember {
        mutableStateOf(5f)
    }

    val split = (splitInput.roundToInt()).toDouble()

    var reset by remember {
        mutableStateOf(false)
    }

    var defaultSelect by remember {
        mutableStateOf(true)
    }

    var roundUpSelect by remember {
        mutableStateOf(false)
    }

    var roundDownSelect by remember {
        mutableStateOf(false)
    }

    val totalTipInDouble = calculateTip(amount, tipPercent)

    val totalTip = roundTheTip(totalTipInDouble, roundUpSelect, roundDownSelect)

    val totalTipAmount = formatValue(totalTipInDouble)

    val totalBillInDouble = amount.plus(totalTipInDouble)

    val totalBillAmount = formatValue(totalBillInDouble)

    val totalBillPerPersonInDouble = split(totalBillInDouble, split)

    val totalBillPerPerson = formatValue(totalBillPerPersonInDouble)

    val totalTipPerPersonInDouble = split(totalTipInDouble, split)

    val totalTipPerPerson = formatValue(totalTipPerPersonInDouble)

    Column(
        modifier = modifier
            .padding(horizontal = 20.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.height(25.dp))
        TipInputCard(
            amountInput = amountInput,
            onChangeAmount = { amountInput = it },
            tipPercentInput = tipPercentInput,
            onChangeTipPercentInput = { tipPercentInput = it },
            splitInput = splitInput,
            onChangeSplitInput = { splitInput = it }
        )
        Spacer(modifier = Modifier.height(50.dp))
        RoundTheTip(
            defaultSelect = defaultSelect,
            roundUpSelect = roundUpSelect,
            roundDownSelect = roundDownSelect,
            defaultValue = {
                defaultSelect = it
                roundUpSelect = false
                roundDownSelect = false
            },
            roundUpValue = {
                roundUpSelect = it
                defaultSelect = false
                roundDownSelect = false
            },
            roundDownValue = {
                roundDownSelect = it
                defaultSelect = false
                roundUpSelect = false
            }
        )
        Spacer(modifier = Modifier.height(50.dp))
        BillAndTipOutputCard(
            totalBill = totalBillAmount,
            totalTip = totalTipAmount,
            totalBillPerPerson = totalBillPerPerson,
            totalTipPerPerson = totalTipPerPerson,
            reset = {
                reset = it
                if (reset) {
                    amountInput = ""
                    tipPercentInput = 15f
                    splitInput = 5f
                    defaultSelect = true
                    roundUpSelect = false
                    roundDownSelect = false
                    reset = false
                }
            }
        )
        Spacer(modifier = Modifier.height(25.dp))
    }
}

@Composable
fun TipInputCard(
    amountInput: String,
    onChangeAmount: (String) -> Unit,
    tipPercentInput: Float,
    onChangeTipPercentInput: (Float) -> Unit,
    splitInput: Float,
    onChangeSplitInput: (Float) -> Unit,
    modifier: Modifier = Modifier
) {

    OutlinedCard(
        modifier = Modifier.fillMaxWidth(),
        border = BorderStroke(3.dp, Color(121, 74, 250))
    ) {
        Column(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(id = R.string.calculate_tip),
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            EditNumberField(
                value = amountInput,
                label = R.string.bill_amount,
                onValueChanged = onChangeAmount,
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                modifier = Modifier.fillMaxWidth()
            )
            TipPercentageColumn(
                tipPercentInput = tipPercentInput,
                onChangeTipPercentInput = onChangeTipPercentInput
            )
            SplitColumn(
                splitInput = splitInput,
                onChangeSplitInput = onChangeSplitInput
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditNumberField(
    @StringRes label: Int,
    value: String,
    onValueChanged: (String) -> Unit,
    keyboardOptions: KeyboardOptions,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChanged,
        label = {
            Text(stringResource(id = label))
        },
        singleLine = true,
        leadingIcon = {
            Icon(
                painter = painterResource(id = R.drawable.baseline_money_24),
                contentDescription = null,
                tint = Color(121, 74, 250)
            )
        },
        keyboardOptions = keyboardOptions,
        modifier = modifier.padding(bottom = 32.dp)
    )
}

@Composable
fun TipPercentageColumn(
    tipPercentInput: Float,
    onChangeTipPercentInput: (Float) -> Unit,
    modifier: Modifier = Modifier
) {

    Column(modifier = Modifier.padding(bottom = 20.dp)) {
        Row {
            Text(
                text = stringResource(id = R.string.how_was_the_service),
                modifier = Modifier.weight(1f)
            )
            Text(
                text = "${tipPercentInput.roundToInt()}%",
                modifier = Modifier.padding(end = 10.dp)
            )
        }
        Slider(
            value = tipPercentInput,
            onValueChange = onChangeTipPercentInput,
            valueRange = 0f..100f,
            colors = SliderDefaults.colors(
                thumbColor = Color(121, 74, 250),
                activeTrackColor = Color(121, 74, 250),
                inactiveTrackColor = Color(219, 207, 250)
            )
        )
    }
}

@Composable
fun SplitColumn(
    splitInput: Float,
    onChangeSplitInput: (Float) -> Unit,
    modifier: Modifier = Modifier
) {
    Column {
        Row {
            Text(
                text = "Split",
                modifier = Modifier.weight(1f)
            )
            Text(
                text = "${splitInput.roundToInt()} "
            )
            Image(
                imageVector = Icons.Filled.Face,
                contentDescription = null,
                colorFilter = ColorFilter.tint(Color(121, 74, 250))
            )
        }
        Slider(
            value = splitInput,
            onValueChange = onChangeSplitInput,
            valueRange = 1f..35f,
            colors = SliderDefaults.colors(
                thumbColor = Color(121, 74, 250),
                activeTrackColor = Color(121, 74, 250),
                inactiveTrackColor = Color(219, 207, 250)
            )
        )
    }
}

@Composable
fun BillAndTipOutputCard(
    totalBill: String,
    totalTip: String,
    totalBillPerPerson: String,
    totalTipPerPerson: String,
    reset: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    ElevatedCard(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp
        ),
        colors = CardDefaults.elevatedCardColors(
            containerColor = Color(121, 74, 250),
            contentColor = Color.White
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            BillAndTipValueRow(
                billAndTipText = "Bill Amount",
                perPerson = "",
                billAndTipValue = totalBill
            )
            BillAndTipValueRow(
                billAndTipText = "Tip Amount",
                perPerson = "",
                billAndTipValue = totalTip
            )
            BillAndTipValueRow(
                billAndTipText = "Bill Amount",
                perPerson = "/ person",
                billAndTipValue = totalBillPerPerson
            )
            BillAndTipValueRow(
                billAndTipText = "Tip Amount",
                perPerson = "/ person",
                billAndTipValue = totalTipPerPerson
            )
            ElevatedButton(
                colors = ButtonDefaults.elevatedButtonColors(
                    containerColor = Color.White,
                    contentColor = Color(121, 74, 250)
                ),
                onClick =  { reset(true) } ,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 45.dp)
            ) {
                Text(text = "Reset")
            }
        }
    }
}

@Composable
fun BillAndTipValueRow(
    billAndTipText: String,
    perPerson: String,
    billAndTipValue: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.padding(bottom = 20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = billAndTipText,
                fontSize = 20.sp
            )
            if (perPerson.isNotEmpty()) {
                Text(
                    text = perPerson,
                    fontSize = 15.sp
                )
            }
        }
        Text(
            text = billAndTipValue,
            fontSize = 30.sp
        )
    }
}

@Composable
fun RoundTheTip(
    defaultSelect: Boolean,
    roundUpSelect: Boolean,
    roundDownSelect: Boolean,
    defaultValue: (Boolean) -> Unit,
    roundUpValue: (Boolean) -> Unit,
    roundDownValue: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Text(
            text = stringResource(id = R.string.round_the_tip),
            color = Color.White,
            fontSize = 35.sp,
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .background(Color(121, 74, 250))
                .wrapContentHeight(Alignment.CenterVertically)
                .wrapContentWidth(Alignment.Start)
                .padding(start = 20.dp)
        )
        Column(
            modifier = Modifier
                .padding(20.dp)
                .selectableGroup()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = defaultSelect,
                    onClick = { defaultValue(true) },
                    colors = RadioButtonDefaults.colors(Color(121, 74, 250))
                )
                Text(text = "Default")
            }
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = roundUpSelect,
                    onClick = { roundUpValue(true) },
                    colors = RadioButtonDefaults.colors(Color(121, 74, 250))
                )
                Text(text = "Round Up")
            }
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = roundDownSelect,
                    onClick = { roundDownValue(true) },
                    colors = RadioButtonDefaults.colors(Color(121, 74, 250))
                )
                Text(text = "Round Down")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    JusTipTheme {
        JusTipItLayout()
    }
}

private fun calculateTip(amount: Double, tipPercent: Double): Double {
    return tipPercent / 100 * amount
}

private fun formatValue(value: Any): String {
    return NumberFormat.getCurrencyInstance().format(value)
}

private fun split(value: Double, split: Double): Double {
    return value.div(value)
}

private fun roundTheTip(
    totalTipInDouble: Double,
    roundUpSelect: Boolean,
    roundDownSelect: Boolean
): Any {
    return if (roundUpSelect) ceil(totalTipInDouble)
    else if (roundDownSelect) floor(totalTipInDouble)
    else totalTipInDouble
}