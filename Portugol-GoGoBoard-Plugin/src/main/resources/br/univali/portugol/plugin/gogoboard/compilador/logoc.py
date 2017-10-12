#
# Tinker Logo Compiler - A lexical analyzer and parser for a
# Logo language for Robotics
#
# Copyright (C) 2014 Chiang Mai University
#  Contact   Arnan (Roger) Sipiatkiat [arnans@gmail.com]
#
# This program is free software; you can redistribute it and/or
# modify it under the terms of the GNU General Public License
# as published by the Free Software Foundation version 2.
#
# This program is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# GNU General Public License for more details.
#
# You should have received a copy of the GNU General Public License
# along with this program; if not, write to the Free Software
# Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
#

LOGO_VERSION    = 1.3

import sys
import ply.lex as lex
import ply.yacc as yacc
import re
#import tkMessageBox

class tinkerLogo:
    def __init__(self):
        pass

    def version(self):
        return LOGO_VERSION

    def compile(self, LogoCodeString):

        # ======================================
        # Run the program through a first-pass
        # parser, which determines the name and
        # parameters of procedures
        # ======================================
        self.fpp = firstPassParser()
        self.logoProcedures = self.fpp.parseProcedures(LogoCodeString)
        self.autoRunState = self.fpp.autoRunState
        #print fpp.procedures_dict

        # ======================================
        # Run the Logo parser
        # ======================================
        self.logo_parser = tinkerParser(self.fpp.procedures_dict, self.fpp.ordered_procedures_list)
        self.logo_parser.parse(LogoCodeString)
        #self.autoRunState = self.logo_parser.autoRunState


        # =====================================================================
        # Post-Processing - Replace ALL symbols with actual procedure addresses
        # =====================================================================

        #self.logo_parser.logoByteCodeString = '0,' + self.logo_parser.logoByteCodeString
        #print "Byte Code String: \r\n" + self.logo_parser.logoByteCodeString + "\r\n"
        #self.logo_parser.logoByteCodeString = self.logo_parser.logoByteCodeString.strip(',')
        # convert the program string into list items by splitting commas and stripping whitespaces
        # note that we remove the trailing ', ' (comma and space)
        self.logo_bytecode_list = [x.strip() for x in self.logo_parser.logoByteCodeString[:-2].split(',')]

        for item_index in range(len(self.logo_bytecode_list)):
            # locate the CALL commands
            # Note that CALL will follow by two address bytes, which are now given placeholders using the procedure name
            # For example, "CALL, test, test" means we want to call a procedure named 'test'
            # we need to replace 'test, test' with two bytes representing a 16-bit address
            if self.logo_bytecode_list[item_index] == 'CALL':
                self.procedure_name = self.logo_bytecode_list[item_index+1]
                self.call_address = self.logo_parser.procedures_address_dict[self.procedure_name]  # retrieve the procedure address
                self.logo_bytecode_list[item_index + 1] = str(self.call_address >> 8)   # high byte
                self.logo_bytecode_list[item_index + 2] = str(self.call_address & 0xff)  # low byte
                item_index += 2

        # if compile error
        if self.logo_bytecode_list[0] == '':
            self.logo_bytecode = ""
            return

        #print self.logo_bytecode_list

        # ==============================================
        # Translate symbols to byte code
        # ==============================================
        # The order of the items are important. It determines the binary code number of the commands
        self.logo_bytecode_lookup = {
             'CODE_END':		0,
             'NUM8':			1,
             'NUM16':			2,
             'LIST':			3,
             'EOL':			    4,
             'EOLR':			5,
             'INPUT':			6,
             'STOP':			7,
             'OUTPUT':			8,
             'REPEAT':			9,
             'IF':			    10,
             'IFELSE':			11,
             'BEEP':			12,
             'NOTE':			13,
             'WAITUNTIL':		14,
             'FOREVER':			15,
             'WAIT':			16,
             'TIMER':			17,
             'RESETT':			18,
             'SEND':			19,
             'IR':			    20,
             'NEWIR':			21,
             'RANDOM':			22,
             'OP_PLUS':			23,
             'OP_MINUS':		24,
             'OP_MULTIPLY':		25,
             'OP_DIVISION':		26,
             'OP_MODULO':	    27,  # i.e. 5 % 3 = 2
             'OP_EQUAL':		28,
             'OP_GREATER':		29,
             'OP_LESS':			30,
             'OP_AND':			31,
             'OP_OR':			32,
             'OP_XOR':			33,
             'OP_NOT':			34,
             'SETGLOBAL':		35,
             'GETGLOBAL':		36,
             'ASET':			37,
             'AGET':			38,
             'RECORD':			39,
             'RECALL':			40,
             'RESETDP':			41,
             'SETDP':			42,
             'ERASE':			43,
             'WHEN':			44,
             'WHENOFF':			45,
             'M_A':			    46,
             'M_B':			    47,
             'IF_STATE_CHANGE':	48,         # executes only when the condition's state changes from false to true
             'ON':			    49,
             'ONFOR':			50,
             'OFF':			    51,
             'THISWAY':			52,
             'THATWAY':			53,
             'RD':			    54,
             'READSENSOR':	    55,
#             'SENSOR2':			56,
             'READSWITCH':	    56,
#             'SWITCH2':			58,
             'SETPOWER':		59,
             'BRAKE':			60,
             'OP_GREATER_OR_EQUAL':			61,     # >=
             'OP_LESS_OR_EQUAL':		    62,     # <=
             'TALK_TO_NODE':    63,         # set the target remote node ID
             'ISON':		    64,         # returns true if specified motor is on
             'ISOFF':		    65,         #         true if specified motor is off
             'ISTHISWAY':		66,         # returns true if direction is thisway
             'ISTHATWAY':	    67,         # returns true if direction is thatway
             'STOPALL':		    68,
             'EB':              69,
             'DB':			    70,
             'LOWBYTE':		    71,
             'HIGHBYTE':		72,
             # 'SENSOR3':			73,
             # 'SENSOR4':			74,
             # 'SENSOR5':			75,
             # 'SENSOR6':			76,
             # 'SENSOR7':			77,
             # 'SENSOR8':			78,
             # 'SWITCH3':			79,
             # 'SWITCH4':			80,
             # 'SWITCH5':			81,
             # 'SWITCH6':			82,
             # 'SWITCH7':			83,
             # 'SWITCH8':			84,
             'LEDON':			85,
             'LEDOFF':			86,
             'SETH':			87,
             'LT':			    88,
             'RT':			    89,
             'TALKTO':			90,
             'GETPOWER':	    91,     # returns current power level
             'CL_I2C_STOP':		92,
             'CL_I2C_WRITE':	93,
             'CL_I2C_READ':		94,
             'SERIAL':			95,
             'NEWSERIAL':		96,
             'RTC_INIT':		97,
             'RTC_GET_ITEM':	98,
             'SHOW':			99,
             'CLS':			    100,
             'GETPOS':			101,
             'SETPOS':			102,
             'TALK_TO_7SEG_1':	103,
             'TALK_TO_7SEG_2':	104,
             'TALK_TO_LCD_1':	105,
             'TALK_TO_LCD_2':	106,
             'I2C_WRITE_REGISTER':			107,
             'I2C_READ_REGISTER':			108,
             'LONG_LIST':		109,
             'CALL':			110 ,
             'STRING':          111,
             'SETTICKRATE':        112,
             'TICKCOUNT':     113,
             'CLEARTICK':       114,

            # Raspberry Pi commands
            # ==========================================
             'USECAMERA':       200,
             'CLOSECAMERA':     201,
             'STARTFINDFACE':   202,
             'STOPFINDFACE':    203,
             'FACEFOUND':       204,
             'TAKESNAPSHOT':    205,
             'CAMERAISON':      206,
             'ISFINDINGFACE':   207,

             'USESMS':          210,
             'SENDSMS':         211,

             'SENDMAIL':        212,
             'SENDSNAPSHOT':    213,

             'PLAYSOUND':       214,
             'STOPSOUND':       215,

             'SHOWIMAGE':       216,
             'SCREENTAPPED':    217,

             'WIFICONNECT':     218,
             'WIFIDISCONNECT':  219,

             'REBOOT':          220,
             'SHUTDOWN':        221,

             # creates a new data record file for the given variable
             'NEWRECORDFILE':   222,
             'RECORD_TO_RPI':     223,
             'SHOWLOGPLOT':      224,

             # RFID byte codes
             'USERFID':              227,
             'CLOSERFID':            228,
             'RFIDBEEP':            229,
             'READRFID':             230,
             'WRITERFID':            231,
             'RFIDTAGFOUND':        232,
             'RFIDREADERFOUND':     233,

              #text to voice
              'SAY':                234,

              # key-value lookup and compare
              'KEY':                 235,
              'OP_KEY_COMPARE':      236,
              'INTKEY':              237,
              'CLEARKEYS':           238,
              'SEND_MESSAGE':        239,


        }

        self.logo_bytecode = ""
        for item in self.logo_bytecode_list:
            if item.isdigit():
                self.logo_bytecode += chr(int(item))
            else:
                self.logo_bytecode += chr(self.logo_bytecode_lookup[item.upper()])

        self.bin_code = ""
        for c in self.logo_bytecode:
            self.bin_code += str(ord(c)) + ", "

        print ("Raw byte code: \r\n" + self.bin_code)

    def byteCode(self):
        ' Returns a binary code string created by the compiler'

        return self.logo_bytecode





class firstPassParser:

    def __init__(self):

        pass

    def parseProcedures(self, sourceText):
        ''' identifies the logo procedures and creates a LogoProcedure object
            for each. It also handles the parameter names.

            returns a list of cmdClasses.LogProcedures Object
        '''

        logoProcedureList = []

        # -----------------------------
        # identify procedures
        # -----------------------------
        #
        # this search will split the procedures into a list. The syntax "to" and "end" are
        # stripped away

        r = re.compile('^[ \t]*to[ \t]+(.*?)^[ \t]*end[ \t]*$', re.MULTILINE | re.IGNORECASE | re.DOTALL )
        procedures = r.findall(sourceText)

        self.procedures_dict = {}
        self.ordered_procedures_list = []  # holds a list of procedure name ordered by the sequence in the code.
                                           # we need it since the procedures_dict by definition does not store
                                           # the items in the same order it was inserted. This causes errors
                                           # in the parser when looking up local variables.
        # --------------------------------------
        # identify procedure name and parameters
        # --------------------------------------
        self.isFirstProcedure = True
        self.autoRunState = False

        for procedureText in procedures:

            print ("Procedure:")
            print ("--------------------------")
            print ("Name: " + procedureText)

            firstLine = re.search('.*?\n', procedureText, re.MULTILINE).group()

            # remove the first line from the procedure
            # make sure the count=1 option is set to prevent accedental
            # removal of lines that are the same as the first line i.e.
            #
            # to tail :i
            # make "i :i + 1
            # tail :i
            # end
            #
            # when to/end are stripped by the first pass parser, we have
            #
            # tail :i
            # make "i :i + 1
            # tail :i
            #
            # this causes us to loose the last command if count=1 is not set

            procedureText = re.sub(firstLine, '', procedureText, count=1)

            # parse the first line
            tokens = re.findall('([:"]*.*?)[ \t\n]+', firstLine)

            if tokens is None:
                # raise an error -> or may be just skip it
                # there is no procedure name. the produre probably looks like this
                #
                #  to
                #  end

                print ("Warning: procedure without a name was found")
                continue

            # first token is the procedure name
            procName = tokens[0]

            if procName.lower() == "autorun" and self.isFirstProcedure:
                self.autoRunState = True

            self.isFirstProcedure = False


            # check if the procedure name is valid
            if not self.nameIsValid(procName):
                # Invalid procedure name
                print ("Error: procedure name %s is invalid" % procName)
                continue


            tokens = tokens[1:]

            parameter_list = []

            # check for parameters
            if len(tokens) > 0:

                for i, parameter in enumerate(tokens):
                    tokens[i] = parameter.lstrip('":')  # remove any preceeding quotes (") or colons (:)
                    parameter_list.append(tokens[i])

            self.procedures_dict[procName] = parameter_list
            self.ordered_procedures_list.append(procName)


##  do this checkig at runtime instead
##
##                    if not self.nameIsValid(tokens[i]):
##                        print "Error: invalid parameter name %s in procedure %s" % (parameter, procName)
##                        # should raise an error




            # put the parsed procedure information into a LogoProcedure object
#            LogoProcedureObj = cmdClasses.LogoProcedure(procName)
#             LogoProcedureObj.parameterNames = tokens
#             LogoProcedureObj.statementsSource = procedureText
#
#             # add the procedure to the procedure list
#             logoProcedureList.append(LogoProcedureObj)
#
#         return logoProcedureList

    def nameIsValid(self, nameString):
        ''' verifies if the nameString is a valid name
            That is, it must begine with alphabets and followed by
            zero or more alphanumeric characters
        '''

        return (re.search('[a-zA-Z_]\w*', nameString) is not None)


class tinkerGoGoLex:

    #Reserved words
    reserved = {

##        'SYMBOL'              ; 'SYMBOL for use in the gramma definition'


#        'to'            : 'TO',
        'end'           : 'END',

        # Core program statements
    #    'aset'          : 'ASET',
    #    'display_show'  : 'DISPLAY_SHOW',
        'forever'       : 'FOREVER',
        'repeat'        : 'REPEAT',
        'if'            : 'IF',
        'ifelse'        : 'IFELSE',
        'ifstatechange' : 'IF_STATE_CHANGE',
        'waituntil'     : 'WAITUNTIL',
        'set'           : 'SET',


        # statements with no arguments
        'stop'          : 'STOP',
        'beep'          : 'BEEP',
        'resett'        : 'RESETT',
        'resetdp'       : 'RESETDP',
        'on'          : 'M_ON',
        'off'         : 'M_OFF',
        'thisway'     : 'M_THISWAY',
        'cw'          : 'M_CW',
        'thatway'     : 'M_THATWAY',
        'ccw'         : 'M_CCW',
        'rd'          : 'M_RD',
        'stopall'     : 'STOPALL',
        'ledon'       : 'ULED_ON',
        'ledoff'      : 'ULED_OFF',
        'rtc_init'      : 'RTC_INIT',
        'cls'           : 'DISPLAY_CLS',
        'talk_to_7seg_1': 'TALK_TO_7SEG_1',
        'talk_to_7seg_2': 'TALK_TO_7SEG_2',
        'talk_to_lcd_1' : 'TALK_TO_LCD_1',
        'talk_to_lcd_2' : 'TALK_TO_LCD_2',

        # statements with one number (or expression) argument
        'output'    : 'OUTPUT',
        'wait'      : 'WAIT',
    #    'send'      : 'SEND',
        'onfor'     : 'ONFOR',
        'record'    : 'RECORD',
        'setdp'     : 'SETDP',
        'erase'     : 'ERASE',
        'setpower'  : 'SETPOWER',
        'seth'      : 'SERVO_SET_H',
        'lt'        : 'SERVO_LT',
        'rt'        : 'SERVO_RT',
        'talk_to_motor'   : 'TALK_TO_MOTOR',
        'setpos'    : 'DISPLAY_SET_POS',
        'show'            : 'DISPLAY_SHOW',
        'i2cwrite'  : 'I2C_WRITE',

        # voice recorder/player module
        'play'      : 'PLAY',
        'nexttrack'      : 'NEXT_TRACK',
        'prevtrack'      : 'PREV_TRACK',
        'gototrack'      : 'GOTO_TRACK',
        'erasetracks'    : 'ERASE_TRACKS',

        # statements with two number arguments
    #    'note'      : 'NOTE',

        # statements with three number arguments
    #    'i2c_write_register'    : 'I2C_WRITE_REGISTER',

        # Expressions with no parameters
        'timer'         : 'TIMER',
        'ir'            : 'IR',
        'recall'        : 'RECALL',
        'serial'        : 'SERIAL',
        'getpos'        : 'DISPLAY_GET_POS',

        # sensor and switch aliases
        'sensor1'       : 'SENSOR1',
        'sensor2'    : 'SENSOR2',
        'sensor3'    : 'SENSOR3',
        'sensor4'    : 'SENSOR4',
        'sensor5'    : 'SENSOR5',
        'sensor6'    : 'SENSOR6',
        'sensor7'    : 'SENSOR7',
        'sensor8'    : 'SENSOR8',

        'switch1'    : 'SWITCH1',
        'switch2'    : 'SWITCH2',
        'switch3'    : 'SWITCH3',
        'switch4'    : 'SWITCH4',
        'switch5'    : 'SWITCH5',
        'switch6'    : 'SWITCH6',
        'switch7'    : 'SWITCH7',
        'switch8'    : 'SWITCH8',

        # real-time-clock commands
        'seconds'       : 'SECONDS',
        'minutes'       : 'MINUTES',
        'hours'         : 'HOURS',
        'dow'           : 'DOW',    # Day of week
        'day'           : 'DAY',
        'month'         : 'MONTH',
        'year'          : 'YEAR', # 0 = the year 2000

        # expression with one parameter
        'readsensor'    : 'READ_SENSOR',
        'readswitch'    : 'READ_SWITCH',
        'random'        : 'RANDOM',
        'lowbyte'       : 'LOWBYTE',
        'highbyte'      : 'HIGHBYTE',
        'i2cread'       : 'I2C_READ',

        'settickrate'   : 'SET_TICK_RATE',
        'tickcount'     : 'TICK_COUNT',
        'cleartick'     : 'CLEAR_TICK',


        # Raspberry Pi commands
        'usecamera'     : 'USE_CAMERA',
        'closecamera'   : 'CLOSE_CAMERA',
        'startfindface' : 'START_FIND_FACE',
        'stopfindface'  : 'STOP_FIND_FACE',
        'takesnapshot'  : 'TAKE_SNAP_SHOT',
        'cameraison'    : 'CAMERA_IS_ON',
        'isfindingface' : 'IS_FINDING_FACE',
        'sendmail'      : 'SEND_MAIL',

        'usesms'        : 'USE_SMS',
        'sendsms'       : 'SEND_SMS',

#        'SENDSNAPSHOT'  : 'SEND_SNAPSHOT',
        'playsound'     : 'PLAY_SOUND',
        'stopsound'     : 'STOP_SOUND',
        'showimage'     : 'SHOW_IMAGE',
        'newrecordfile' : 'NEW_RECORD_FILE',
        'showlogplot'   : 'SHOW_LOG_PLOT',

        'userfid'       : 'USE_RFID',
        'closerfid'      : 'CLOSE_RFID',
        'rfidbeep'      : 'RFID_BEEP',
        'readrfid'      : 'RFID_READ',
        'writerfid'     : 'RFID_WRITE',

        'say'           : 'SAY',

        'ison'          : 'IS_ON',
        'isoff'         : 'IS_OFF',
        'isthisway'     : 'IS_THISWAY',
        'isthatway'     : 'IS_THATWAY',
        'getpower'      : 'GET_POWER',

        'key'   : 'GET_KEY_VALUE',
        'intkey': 'GET_KEY_INT_VALUE',
        'clearkeys': 'CLEARKEYS',


        'sendmessage': 'SEND_MESSAGE',
    }

    tokens =  \
        [
        'NAME', 'NUMBER',
    #    'LIST_BLOCK',
        'OP_PLUS', 'OP_MINUS', 'OP_MULTIPLY', 'OP_DIVISION', 'OP_MODULO',
        'LPAREN', 'RPAREN', 'LBRACKET','RBRACKET',
        'OP_LESS', 'OP_LESS_OR_EQUAL','OP_GREATER', 'OP_GREATER_OR_EQUAL', 'OP_EQUAL', 'OP_AND', 'OP_OR', 'OP_XOR','OP_NOT',
        'NEWIR', 'NEWSERIAL',
        'FACE_FOUND', 'SCREEN_TAPPED',
        'RFID_TAG_FOUND', 'RFID_READER_FOUND',
        'TALKTO',
        'TALK_TO_NODE', 'NODE_ID',
        'PROCEDURE_NAME_DECLARATION',
        'PARAMETER',
        'STRING',
        # 'COMMA',
        # 'NEWLINE',
        #'DELIMITER',
        'PROCECURE_CALL_0_PARAM',
        'PROCECURE_CALL_1_PARAM',
        'PROCECURE_CALL_2_PARAM',
        'PROCECURE_CALL_3_PARAM',
        'PROCECURE_CALL_4_PARAM',
        'PROCECURE_CALL_5_PARAM',
        'PROCECURE_CALL_6_PARAM',
        ] + list(reserved.values())

    # Tokens

    # t_OP_PLUS    = r'\+'
    # t_OP_MINUS   = r'-'
    # t_OP_MULTIPLY= r'\*'
    # t_OP_DIVISION  = r'/'
    # t_OP_EQUAL  = r'='
    t_LPAREN  = r'\('
    t_RPAREN  = r'\)'
    t_LBRACKET = r'\['
    t_RBRACKET = r'\]'
    # t_OP_LESS    = r'<'
    # t_OP_GREATER = r'>'


    def __init__(self, procedures_dict):
        self.lexer = lex.lex(module=self)

        self.procedures_dict = procedures_dict


    def t_COMMENT(self,t):
        r';.*'
        pass

    def t_OP_PLUS(self,t):
        r'\+'
        t.value = "OP_PLUS"
        return t
    def t_OP_MINUS(self,t):
        r'-'
        t.value = "OP_MINUS"
        return t
    def t_OP_MULTIPLY(self,t):
        r'\*'
        t.value = "OP_MULTIPLY"
        return t
    def t_OP_DIVISION(self,t):
        r'/'
        t.value = "OP_DIVISION"
        return t
    def t_OP_MODULO(self,t):
        r'%'
        t.value = "OP_MODULO"
        return t
    def t_OP_GREATER_OR_EQUAL(self, t):
        r'>='
        t.value = "OP_GREATER_OR_EQUAL"
        return t
    def t_OP_LESS_OR_EQUAL(self, t):
        r'<='
        t.value = "OP_LESS_OR_EQUAL"
        return t

    def t_OP_LESS(self,t):
        r'<'
        t.value = "OP_LESS"
        return t
    def t_OP_GREATER(self,t):
        r'>'
        t.value = "OP_GREATER"
        return t
    def t_OP_EQUAL(self,t):
        r'='
        t.value = "OP_EQUAL"
        return t
    def t_OP_AND(self,t):
        r'and'
        t.value = "OP_AND"
        return t
    def t_OP_OR(self,t):
        r'or'
        t.value = "OP_OR"
        return t
    def t_OP_XOR(self,t):
        r'xor'
        t.value = "OP_XOR"
        return t
    def t_OP_NOT(self,t):
        r'not'
        t.value = "OP_NOT"
        return t

    def t_PARAMETER(self, t):
        r'\:[a-zA-Z_][a-zA-Z0-9_]*'
        return t

    def t_NUMBER(self, t):
        r'\d+'
        try:
            t.value = int(t.value)
            if t.value < 256:
                t.value = 'NUM8, '+str(t.value)
            else:
                t.value = 'NUM16, '+str(t.value >> 8)+', '+str(t.value & 0xff)

        except ValueError:
            print("Integer value too large %d", t.value)
            t.value = 0
        return t

    # Ignored characters
    t_ignore = " \t"

    def t_newline(self, t):
        r'\n+'
        t.lexer.lineno += t.value.count("\n")

    def t_TALKTO(self, t):
        r'[abcd]+\,'
        t.type = "TALKTO"
        return t

    def t_TALK_TO_NODE(self,t):
        r'talkto'
        t.type = "TALK_TO_NODE"
        t.value = "TALK_TO_NODE"
        return t

    def t_ISON(self, t):
        r'[abcd]+on\?'
        t.type = "IS_ON"
        return t

    def t_ISOFF(self, t):
        r'[abcd]+off\?'
        t.type = "IS_OFF"
        return t


    def t_ISTHISWAY(self, t):
        r'[abcd]+thisway\?'
        t.type = "IS_THISWAY"
        return t

    def t_ISCCW(self, t):
        r'[abcd]+ccw\?'
        t.type = "IS_THATWAY"
        t.value = t.value[:-4] + 'thatway?'
        #print "TEST " + t.value
        return t

    def t_ISCW(self, t):
        r'[abcd]+cw\?'
        t.type = "IS_THISWAY"
        t.value = t.value[:-3] + 'thisway?'
        return t


    def t_ISTHATWAY(self, t):
        r'[abcd]+thatway\?'
        t.type = "IS_THATWAY"
        return t



    def t_GETPOWER(self, t):
        r'[abcd]power'
        t.type = "GET_POWER"
        return t


    def t_NODE_ID(self, t):
        r'n[0-9][0-9]*'
        t.type = "NODE_ID"
        t.value = int(t.value[1:])  # remove the preceeding 'n'
        return t


    def t_NEWIR(self, t):
        r'newir\?'
        t.type = "NEWIR"
        t.value = "NEWIR"
        return t

    def t_NEWSERIAL(self, t):
        r'newserial\?'
        t.type = "NEWSERIAL"
        t.value = "NEWSERIAL"
        return t

    def t_FACE_FOUND(self, t):
        r'facefound\?'
        t.type = "FACE_FOUND"
        t.value = "FACEFOUND"
        return t

    def t_SCREEN_TAPPED(self, t):
        r'screentapped\?'
        t.type = "SCREEN_TAPPED"
        t.value = "SCREENTAPPED"
        return t

    def t_RFID_TAG_FOUND(self, t):
        r'rfidtagfound\?'
        t.type = "RFID_TAG_FOUND"
        t.value = "RFIDTAGFOUND"
        return t

    def t_RFID_READER_FOUND(self, t):
        r'rfidreaderfound\?'
        t.type = "RFID_READER_FOUND"
        t.value = "RFIDREADERFOUND"
        return t

    def t_PROCEDURE_NAME_DECLARATION(self,t):
        r'to(\s)+[a-zA-Z_][a-zA-Z_0-9]*'
        t.value = t.value[2:].lstrip()

        return t

    def t_STRING(self, t):
        r'".*?"'
        # the '?' makes the match non-greedy. This helps when more than one string is on the same line
        t.type = "STRING"
        t.value = t.value[1:-1] # return the string without quotes
        return t

    def t_ID(self, t):
        r'[a-zA-Z_][a-zA-Z_0-9]*'
        if t.value in self.reserved:
            t.type = self.reserved.get(t.value,'ID')    # Check for reserved words

        # if the name is a procedure call -> set the type to PROCEDURE_CALL_X_PARAM
        # where X is the number of parameters. The number of parameters is kept
        # in the procedures_dict
        elif t.value in self.procedures_dict:
            t.type = "PROCECURE_CALL_" + str(len(self.procedures_dict[t.value])) + "_PARAM"
            self.showLog ("Found procedure call. Type = " + t.type + ", value = " + t.value)

        else:
            t.type = "NAME"
        return t


    def showLog(self, message):
        pass
        # print message

    def t_error(self, t):
        print("Illegal character '%s'" % t.value[0])
        t.lexer.skip(1)



    # lex.lex()
    # lex.input(logoString)
    # print logoString
    #
    # while True:
    #     tok = lex.token()
    #     if not tok: break
    #     print tok.type + "," + tok.value








class tinkerParser:

    # =========================================================
    # Parsing rules
    # =========================================================
    precedence = (
        ('left','OP_AND', 'OP_OR', 'OP_XOR'),
        ('left','OP_LESS', 'OP_LESS_OR_EQUAL', 'OP_GREATER','OP_GREATER_OR_EQUAL', 'OP_EQUAL'),

        ('right', 'OP_NOT'),

        ('left','OP_PLUS','OP_MINUS'),
        ('left','OP_MULTIPLY','OP_DIVISION', 'OP_MODULO'),
    #    ('right','UMINUS'),

        ('right', 'RANDOM', 'LOWBYTE', 'HIGHBYTE', 'I2C_READ', 'READ_SENSOR', 'READ_SWITCH'),

        ('right', 'NAME'),
        ('right', 'PROCECURE_CALL_0_PARAM', 'PROCECURE_CALL_1_PARAM', 'PROCECURE_CALL_2_PARAM',
                  'PROCECURE_CALL_3_PARAM', 'PROCECURE_CALL_4_PARAM', 'PROCECURE_CALL_5_PARAM', 'PROCECURE_CALL_6_PARAM',
                  'I2C_WRITE'

        )
    )




    def __init__(self, procedurs_dict, ordered_procedures_list):
        self.lexer = tinkerGoGoLex(procedurs_dict)
        self.tokens = self.lexer.tokens
        self.parser = yacc.yacc(module=self,debug=True)
        self.procedures_dict = procedurs_dict
        self.ordered_procedures_list = ordered_procedures_list
        self.procedures_address_dict = {}  # stores the byte address of each procedure
        self.current_procedure_index = 0  # current procedure index of the procedures_dict
        self.logoByteCodeString = "" # stores the compiled procedure string. It will be later translated into
                                  # the binary code.
        self.procedure_len_counter = 0 # tracks the procedure length. Used to calculate the procedure call address
        self.lineno = 0
        self.procedures = []
        self.autoRunState = False

        self.if_state_change_counter = 0  # used to assign a unique id for each if-state-change command. This id
                                          # is used in the logo vm to track the state of the condition

    def parse(self, data):
        if data:
            return self.parser.parse(data,self.lexer.lexer,0,0,None)
        else:
            return []


    def p_procedures(self, t):
        ''' procedures : procedure
                        | procedure procedures
        '''

        if len(t) == 2:
            t[0] = t[1]
        elif len(t) == 3:
            t[0] = t[1]


    def p_procedure(self, t):
        ''' procedure : PROCEDURE_NAME_DECLARATION statements END
                      | PROCEDURE_NAME_DECLARATION parameters statements END
        '''

        self.showLog("-> Procedure: " + t[1])
        if t[1] in self.procedures:
            self.showLog("Error: Procedure " + t[1] + " already defined.")
        else:
            self.procedures.append(t[1])


        # if this is the first procedure -> it will be run when the 'run' button is pressed.
        # make sure we stop the program when this procedure ends
        main_procedure = True if self.procedures[0] == t[1] else False



        if len(t) == 4:
            t[0] =  t[2]
        elif len(t) == 5:
            t[0] =  t[3]
            self.showLog("Prameters = " + t[2])



        # ----------------------------------------------------
        # Clean up
        # ----------------------------------------------------
        # Remove any preceding commas

        #if t[0][0] == ',':
        #    t[0] = t[0][1:]
        t[0] = t[0].lstrip(',')

        # Remove any empty commands ", ,"
        t[0] = t[0].replace(", ,",",")


        # ----------------------------------------------------
        # Add procedure parameter count
        # ----------------------------------------------------

        # The procedure body starts with a number indicating how many parameters it has.
        # We look up this value from the procedures_dict.
        if not main_procedure:
            t[0] = str(len(self.procedures_dict[t[1]])) + ", " + t[0]

        # ----------------------------------------------------
        # Add code end
        # ----------------------------------------------------
        if main_procedure == True:
            t[0] += ", CODE_END, "
        else:
            t[0] += ", STOP, "


        self.procedures_address_dict[t[1]]= self.procedure_len_counter
        self.procedure_len_counter += len(t[0][:-2].split(','))

        self.showLog("Procedure Content = " + t[0])

        self.logoByteCodeString += t[0]
        self.current_procedure_index += 1   # point to the next procedure. Used in processing variables

    def p_statements(self, t):
        ''' statements : statement
                       | statement statements
        '''
    #    self.showLog("----------")
    #     for c in t:
    #         print c

        if len(t) == 3:
    #        self.showLog("--> statement statements")
            t[0] = t[1].rstrip(',') + ", " + t[2].lstrip(',')
        elif len(t) == 2:
    #        self.showLog("-->statement")
            t[0] = t[1]
        else:
    #        self.showLog("--> not matched")
            pass

    # def p_statement_expr(self, t):
    #     'statement : expression'
    #     t[0] = t[1]

    def p_expression_statement(self, t):
        'expression : procedure_call'
        t[0] = t[1]



    def p_parameters(self, t):
        ''' parameters : PARAMETER
                        | PARAMETER parameters
        '''

        if len(t) == 2:
            t[0] = t[1]
        elif len(t) == 3:
            t[0] = t[1].rstrip(',') + ", " + t[2].lstrip(',')



    # def p_statement_assign(self, t):
    #     'statement : NAME EQUALS expression'
    #     names[t[1]] = t[3]

    # list of names
    global_variables = []

    def p_statement_assign(self, t):
        'statement : SET NAME expression'

        if not t[2] in self.global_variables:
            self.global_variables.append(t[2])
        t[0] = "NUM8, " + str( self.global_variables.index(t[2])) + ", " + t[3] + ", SETGLOBAL"
        print ("Global: " + str( self.global_variables.index(t[2])))


    def p_statement_talkto_node(self, t):
        ''' statement : TALK_TO_NODE expression_node_name
                      | TALK_TO_NODE expression
        '''

        t[0] = t[2] + "," + t[1]

    # def p_statement_talkto_node(self, t):
    #     ' statement : TALK_TO_NODE NODE_ID'
    #
    #     t[0] = 'NUM16, ' + t[2] + ', ' + t[1]


    def p_statement_procedure_call(self,t):
        'statement : procedure_call'
        t[0] = t[1]

    # def p_statement_error(self,t):
    #     'p_eror'
    #     print "I don't know how to " + t.value
    #     self.stopParser()


    def p_procedure_call(self,t):
        '''
              procedure_call : PROCECURE_CALL_0_PARAM
                        | PROCECURE_CALL_1_PARAM expression
                        | PROCECURE_CALL_2_PARAM expression expression
                        | PROCECURE_CALL_3_PARAM expression expression expression
                        | PROCECURE_CALL_4_PARAM expression expression expression expression
                        | PROCECURE_CALL_5_PARAM expression expression expression expression expression
                        | PROCECURE_CALL_6_PARAM expression expression expression expression expression expression
        '''

        # this is justa place holder for a procedure call.
        # it will be replaced with CALL, Address-highbyte, Address-lowbyte
        # in post-processing
        call_phrase = ", CALL, "+t[1]+", "+t[1]

        if len(t) == 2:
            t[0] = "CALL, "+t[1]+", "+t[1]
        if len(t) == 3:
            t[0] = t[2] + call_phrase
        if len(t) == 4:
            t[0] = t[3] + ', ' + t[2] + call_phrase
        if len(t) == 5:
            t[0] = t[4] + ', ' + t[3] + ', ' + t[2] + call_phrase
        if len(t) == 6:
            t[0] = t[5] + ', ' + t[4] + ', ' + t[3] + ', ' + t[2] + call_phrase
        if len(t) == 7:
            t[0] = t[6] + ', ' + t[5] + ', ' + t[4] + ', ' + t[3] + ', ' + t[2] + call_phrase
        if len(t) == 8:
            t[0] = t[7] + ', ' + t[6] + ', ' + t[5] + ', ' + t[4] + ', ' + t[3] + ', ' + t[2] + call_phrase



    def p_statement_unary(self, t):
        ''' statement : STOP
                      | BEEP
                      | RESETT
                      | RESETDP
                      | M_ON
                      | M_OFF
                      | M_THISWAY
                      | M_CW
                      | M_THATWAY
                      | M_CCW
                      | M_RD
                      | STOPALL
                      | ULED_ON
                      | ULED_OFF
                      | RTC_INIT
                      | DISPLAY_CLS
                      | TALK_TO_7SEG_1
                      | TALK_TO_7SEG_2
                      | TALK_TO_LCD_1
                      | TALK_TO_LCD_2
                      | CLEAR_TICK
        '''

        if t[1].lower() == 'cw':
            t[0] = 'thisway'
        elif t[1].lower() == 'ccw':
            t[0] = 'thatway'
        else:
            t[0] = t[1]

    def p_statement_one_parameter(self, t):
        '''statement :   OUTPUT expression
                     |   ONFOR expression
                     |   WAIT expression
                     |   RECORD expression
                     |   SETDP expression
                     |   ERASE expression
                     |   SETPOWER expression
                     |   SERVO_SET_H expression
                     |   SERVO_LT expression
                     |   SERVO_RT expression
                     |   TALK_TO_MOTOR expression
                     |   DISPLAY_SET_POS expression
                     |   DISPLAY_SHOW expression
                     |   SET_TICK_RATE expression
         '''

        if t[1] == 'show':
            t[0] = t[2] + ', NUM8, 2, ' + t[1]    # 2 tells the display module to show a number (not text)
        else:
            t[0] = t[2] + ', ' + t[1]

    def p_statement_record_to_raspberrypi(self,t):
        '''statement : RECORD expression string_expression
        '''

        t[0] = t[2] + ', ' + t[3] + ', ' + 'RECORD_TO_RPI'

    def p_statement_send_int_message_to_raspberrypi(self,t):
        '''statement : SEND_MESSAGE string_expression expression
        '''

        t[0] = t[3] + ', NUM8, 1,' + t[2] + ', ' + 'SEND_MESSAGE'


    def p_statement_send_string_message_to_raspberrypi(self,t):
        '''statement : SEND_MESSAGE string_expression string_expression
        '''

        t[0] = t[3] + ', NUM8, 2,' + t[2] + ', ' + 'SEND_MESSAGE'


    def p_statement_i2c(self,t):
        ''' statement  : I2C_WRITE expression expression expression'''

        t[0] = t[4] + ', ' + t[3] + ', ' + t[2] + ', I2C_WRITE_REGISTER'



    def p_statement_show_string(self, t):
        ''' statement   :   DISPLAY_SHOW string_expression
        '''

        string_length = len(t[2].split(',')) - 3  # string format is 'STRING','len','char1','char2',...,'char len'
                                                  # so we need to minus 2 for the headers and another 1 for the
                                                  # trailing comma

        if string_length <= 4:
            t[0] = t[2] + ', NUM8, 3, ' + t[1]    # 3 tells the display module to show a short 4 character text

        else:
            # display long text (for the 16x2 module only)
            t[0] = t[2] + ', NUM8, 5, ' + t[1]    # 5 tells the display module to show a long text


    def p_statement_rpi_send_mail(self,t):
        ''' statement   :   SEND_MAIL string_expression string_expression string_expression
        '''

        # send_mail address, title, body

        string_length = len(t[2].split(',')) - 3
        string_length += len(t[3].split(',')) - 3
        string_length += len(t[4].split(',')) - 3

        if string_length > 100:
            print ("Warning. Text Length for SEND_MAIL is long. This could cause the program to fail.")

        t[0] = t[4] + ',' + t[3] + ',' + t[2] + ',' + t[1]
        #t[0] = t[2] + t[1]

    def p_statement_rpi_send_sms(self,t):
        ''' statement   :   SEND_SMS string_expression string_expression
        '''
        # t[0] = mail subject + mail-to address + send_mail

        string_length = len(t[3])

        if string_length > 50:
            print ("Warning. Text Length for SEND_SMS is long. This could cause the program to fail.")

        t[0] = t[3] + ',' + t[2] + ',' + t[1]





    def p_statement_rpi_one_string_arg(self,t):
        ''' statement   :   PLAY_SOUND   string_expression
                        |   SHOW_IMAGE   string_expression
                        |   NEW_RECORD_FILE  string_expression
                        |   SAY          string_expression

        '''

        t[0] = t[2] + ',' + t[1]

    def p_statement_rpi_show_log_plot(self,t):
        ''' statement   :  SHOW_LOG_PLOT  expression string_expression
                        |  SHOW_LOG_PLOT  string_expression
        '''

        # plot N latest values
        if len(t) == 4:
            t[0] = t[2] + ',' + t[3] + ',' + t[1]
        # plot all values ( 0 = All)
        else:
            t[0] = 'NUM8, 0,' + t[2] + ',' + t[1]

    def p_statement_voice_player(self,t):
        '''  statement :    PLAY
                       |   NEXT_TRACK
                       |   PREV_TRACK
                       |   GOTO_TRACK expression
                       |   ERASE_TRACKS
        '''

        if t[1] == 'play':
            t[0] = 'NUM8, 6, NUM8, 1, NUM8, 184, I2C_WRITE_REGISTER'
        elif t[1] == 'nexttrack':
            t[0] = 'NUM8, 9, NUM8, 1, NUM8, 184, I2C_WRITE_REGISTER'
        elif t[1] == 'prevtrack':
            t[0] = 'NUM8, 18, NUM8, 1, NUM8, 184, I2C_WRITE_REGISTER'
        elif t[1] == 'gototrack':
            t[0] = t[2] + ', NUM8, 48, OP_PLUS, NUM8, 3, NUM8, 184, I2C_WRITE_REGISTER'
        elif t[1] == 'erasetracks':
            t[0] = 'NUM8, 12, NUM8, 1, NUM8, 184, I2C_WRITE_REGISTER'

        # add a delay to allow time for the voice recorder to execute
        t[0] = t[0] + ', NUM8, 10, WAIT'

    def p_statement_rpi_rfid(self, t):
        ''' statement : RFID_WRITE expression
        '''

        t[0] = t[2] + "," + t[1]


    def p_statement_rpi_unary(self, t):
        ''' statement : USE_CAMERA
                      | CLOSE_CAMERA
                      | START_FIND_FACE
                      | STOP_FIND_FACE
                      | TAKE_SNAP_SHOT
                      | STOP_SOUND
                      | USE_RFID
                      | CLOSE_RFID
                      | RFID_BEEP
                      | USE_SMS
                      | CLEARKEYS
        '''
        t[0] = t[1]



    def p_statement_forever(self, t):
        'statement : FOREVER list'
        t[0] = t[2] + ", FOREVER"

    def p_statement_repeat(self, t):
        'statement : REPEAT expression list'
        t[0] = t[2] + t[3] + ", REPEAT"

    def p_statement_waituntil(self, t):
        'statement : WAITUNTIL expression_list'

        t[0] = t[2] + ", WAITUNTIL"


    def p_statement_if(self, t):
        'statement : IF expression list'
        t[0] = t[2] + t[3] + ", IF"

    def p_statement_if_state_change(self, t):
        'statement : IF_STATE_CHANGE expression list'

        # this if only executes the list when the condition state changes from false to true
        # the firmware tracks the current state using the unique id assigned by self.if_state_change_counter
        t[0] = 'NUM8, '+ str(self.if_state_change_counter) + ", " + t[2] + t[3] + ", IF_STATE_CHANGE"
        self.if_state_change_counter += 1


    def p_statement_ifelse(self, t):
        '''  statement : IFELSE expression list list
        '''

        if len(t) == 7:
            t[0] = t[2] + t[3] + t[6] + ", IFELSE"
        elif len(t) == 5:
            t[0] = t[2] + t[3] + t[4] + ", IFELSE"

    def p_statement_talkto(self, t):
        ' statement : TALKTO '

        t[0] = self.create_talkto_bytecode(t[1])


    def p_expression_ison_isoff(self, t):
        ''' expression : IS_ON
                       | IS_OFF
        '''
        t[0] = self.create_motor_state_reporter_bytecode(t[1])

    def p_expression_isthisway(self,t):
        ' expression : IS_THISWAY'
        t[0] = self.create_motor_state_reporter_bytecode(t[1])

    def p_expression_isthatway(self,t):
        ' expression : IS_THATWAY'
        t[0] = self.create_motor_state_reporter_bytecode(t[1])

    def p_expression_getpower(self,t):
        ' expression : GET_POWER'
        t[0] = self.create_motor_state_reporter_bytecode(t[1])
        if t[0] == "ERROR":
            print ("Error in Get Power: cannot read power from more than one port")
            self.stopParser()


    def p_list(self, t):
        ' list : list_open statements list_close '

        # stip(',') removes any empty tokens
        list_len = len(t[2].strip(',').split(','))+1
        #print "statement list = " + t[2]
        #print "len = " + str(list_len)

        # LONG_LIST is used for lists tha are longer than 256 bytes
        # Note that the original Cricket Logo does not have LONG_LISTs
        if  list_len < 256:
            t[0] = ", LIST, " + str(list_len) + ", " + t[2] + ", EOL"
        else:
            t[0] = ", LONG_LIST, " + str(list_len >> 8) + ", " + str(list_len & 0xff) + ", " + t[2] + ", EOL"


    def p_expression_list(self,t):
        ' expression_list : list_open expression list_close'

        # stip(',') removes any empty tokens
        list_len = len(t[2].strip(',').split(','))+1
        #print "expression list = " + t[2]
        #print "len = " + str(list_len)



        # LONG_LIST is used for lists tha are longer than 256 bytes
        # Note that the original Cricket Logo does not have LONG_LISTs
        if  list_len < 256:
            t[0] = ", LIST, " + str(list_len) + ", " + t[2] + ", EOLR"
        else:
            t[0] = ", LONG_LIST, " + str(list_len >> 8) + ", " + str(list_len & 0xff) + ", " + t[2] + ", EOLR"


    def p_list_open(self, t):
        ''' list_open  : LBRACKET
        '''
        t[0] = "["

    def p_list_close(self, t):
        ''' list_close  : RBRACKET
        '''

        # we don't declare
        #   'delimiters RBRACKET' because statements have already been declared to proceed with delimiters
        #   'RBRACKET delimiters' because it is possible for a list to terminate a statement. And statements
        #    have been declared to accept proceeding delimiters

        t[0] = "]"



    # # this procedure cannot be moved in front of p_statemets(t)
    # # it will cause an error.
    # def p_delimiters(self, t):
    #     ''' delimiters  : DELIMITER
    #                     | DELIMITER delimiters
    #     '''
    #     t[0] = "DELIMITER"

    # =======================================
    # Expressions
    # =======================================

    def p_expression_binop(self, t):
        '''expression : expression OP_PLUS expression
                      | expression OP_MINUS expression
                      | expression OP_MULTIPLY expression
                      | expression OP_DIVISION expression
                      | expression OP_MODULO expression
                      | expression OP_LESS expression
                      | expression OP_LESS_OR_EQUAL expression
                      | expression OP_GREATER expression
                      | expression OP_GREATER_OR_EQUAL expression
                      | expression OP_EQUAL expression
                      | expression OP_AND expression
                      | expression OP_OR expression
                      | expression OP_XOR expression
        '''

        t[0] = t[1] + ', ' + t[3] + ', ' + t[2]


    # def p_expression_uminus(self, t):
    #     'expression : MINUS expression %prec UMINUS'
    #     # t[0] = -t[2]
    #     t[0] = '-' + t[2]

    def p_key_compare_expression(self, t):
        '''expression :  GET_KEY_EXPRESSION OP_EQUAL string_expression
        '''
        t[0] = t[1] + ', ' + t[3] + ',' + 'OP_KEY_COMPARE'



    def p_expression_node_name(self, t):
        'expression_node_name : NODE_ID'
        t[0] = 'NUM16, ' + str(t[1] >> 8) + ', ' + str(t[1] & 0xff)


    def p_expression_group(self, t):
        'expression : LPAREN expression RPAREN'
        t[0] = t[2]

    def p_expression_number(self, t):
        'expression : NUMBER'
        t[0] = t[1]

    def p_expression_name(self, t):
        'expression : NAME'

        #print "=-=-=-"
#        print self.procedures_dict.keys()
#        print self.current_procedure_index
        #current_procedure_name = self.procedures_dict.keys()[self.current_procedure_index]
        current_procedure_name = self.ordered_procedures_list[self.current_procedure_index]



        # Check if NAME is a local variable
        if t[1] in self.procedures_dict[current_procedure_name]:
            t[0] = 'INPUT, ' + str(self.procedures_dict[current_procedure_name].index(t[1]))
            self.showLog("Found LOCAL variable " + t[1] + " in procedure " + current_procedure_name)
        # Check if NAME is a global variable
        elif t[1] in self.global_variables:
            t[0] = "NUM8, " + str( self.global_variables.index(t[1])) + ", GETGLOBAL"
            self.showLog("Found GLOBAL variable " + t[1] + " in procedure " + current_procedure_name)
        else:
            print ("I don't know the expression '%s' at line %s" % (t[1], t.lineno(1)))
            #print ("This procedure's parameters = " + self.procedures_dict[current_procedure_name])
            tkMessageBox.showerror ("Compile Error", "I don't know the expression '%s' at line %s" % (t[1], t.lineno(1)))
            # should raise an error here
            t[0] = "NAME-ERROR"
#            raise SyntaxError
            self.stopParser()


    def p_expression_string(self, t):
        'string_expression : STRING'

        t[0] = "STRING, " + str(len(t[1])) + ", "

        for char in t[1]:
            t[0] = t[0] + str(ord(char)) + ', '

        t[0] = t[0][:-2] # remove the trailing ', '


    def p_expression_rpi_unary(self,t):
        ''' expression      : FACE_FOUND
                            | CAMERA_IS_ON
                            | IS_FINDING_FACE
                            | SCREEN_TAPPED
                            | RFID_READ
                            | RFID_TAG_FOUND
                            | RFID_READER_FOUND
        '''

        t[0] = t[1]


    def p_expression_no_argument(self, t):
        '''expression   : TIMER
                          |  IR
                          |  RECALL

                          |  SERIAL
                          |  DISPLAY_GET_POS

                          |  NEWIR
                          |  NEWSERIAL
                          |  TICK_COUNT



        '''
        t[0]=t[1]

    def p_expression_sensor_aliases(self, t):
        '''expression   :    SENSOR1
                          |  SENSOR2
                          |  SENSOR3
                          |  SENSOR4
                          |  SENSOR5
                          |  SENSOR6
                          |  SENSOR7
                          |  SENSOR8

        '''
        t[0]= "NUM8, " + t[1][6:] + ", readsensor"

    def p_expression_switch_aliases(self, t):
        '''expression   :    SWITCH1
                          |  SWITCH2
                          |  SWITCH3
                          |  SWITCH4
                          |  SWITCH5
                          |  SWITCH6
                          |  SWITCH7
                          |  SWITCH8

        '''
        t[0]= "NUM8, " + t[1][6:] + ", readswitch"


    def p_expression_key_compare(self, t):
        ''' GET_KEY_EXPRESSION : GET_KEY_VALUE string_expression
        '''

        t[0] = t[2] + "," + t[1]


    def p_expression_key_int_value(self,t):
        ''' expression : GET_KEY_INT_VALUE string_expression
        '''
        t[0] = t[2] + ',' + t[1]

    def p_clock_expressions(self,t ):
        ''' expression  :  SECONDS
                          | MINUTES
                          | HOURS
                          | DOW
                          | DAY
                          | MONTH
                          | YEAR
        '''

        if t[1] == 'seconds':
            item = 0
        elif t[1] == 'minutes':
            item = 1
        elif t[1] == 'hours':
            item = 2
        elif t[1] == 'dow':
            item = 3
        elif t[1] == 'day':
            item = 4
        elif t[1] == 'month':
            item = 5
        elif t[1] == 'year':
            item = 6

        t[0] = 'NUM8, ' + str(item) + ', RTC_GET_ITEM'


    def p_expression_one_parameter(self, t):
        ''' expression      :    LOWBYTE expression
                             |   HIGHBYTE expression
                             |   OP_NOT expression
                             |   RANDOM expression
                             |   READ_SENSOR expression
                             |   READ_SWITCH expression
        '''
        t[0] = t[2] + ", " + t[1]

    def create_talkto_bytecode(self, inString):
        ' converts abcd: -> NUM8 0b1111 TALKTO'

        inString = inString[:-1]  # remove the trailing colon

        talk_to_value = 0
        for m in inString:
            if m == 'a':
                talk_to_value |= 1
            elif m == 'b':
                talk_to_value |= 2
            elif m == 'c':
                talk_to_value |= 4
            elif m == 'd':
                talk_to_value |= 8

        return "NUM8, " + str(talk_to_value) + ", TALKTO"


    def create_motor_state_reporter_bytecode(self, inString):

        if inString[-5:] != "power":
            inString = inString[:-1].lower()  # remove the trailing question mark


        talk_to_value = 0

        if inString[-2:] == "on":
            portString = inString[:-2]
            cmd = "ison"
        elif inString[-3:] == "off":
            portString = inString[:-3]
            cmd = "isoff"
        elif (inString[-7:] == "thisway"):
            portString = inString[:-7]
            cmd = "isthisway"
        elif (inString[-7:] == "thatway"):
            portString = inString[:-7]
            cmd = "isthatway"
        elif (inString[-5:] == "power"):
            portString = inString[:-5]
            cmd = "getpower"
            if len(portString) > 1: # can't request power level from more than one port
                return "ERROR"

        for m in portString:
            if m == 'a':
                talk_to_value |= 1
            elif m == 'b':
                talk_to_value |= 2
            elif m == 'c':
                talk_to_value |= 4
            elif m == 'd':
                talk_to_value |= 8

        return "NUM8, " + str(talk_to_value) + ", " + cmd.upper()



    def p_expression_i2cread(self,t):
        ''' expression : I2C_READ expression expression
        '''

        t[0] = t[3] + ', ' + t[2] + ', I2C_READ_REGISTER'


    def showLog(self, text):
        pass
        #print text

    def p_error(self, t):
        print( "Line %s: Syntax error at '%s'" % (t.lineno, t.value))
        self.stopParser()
        tkMessageBox.showerror ("Compile Error", "At line %s, I don't understand '%s' or what came before it." % (t.lineno, t.value))

    def stopParser(self):

        try:
            # Stop parsing by eating up all the remaining tokens.
            while yacc.token() != None:
                pass

        except:
            pass


# =================================================================================
# The following example is a modification of the official example that 
# demonstrates how to use the Logo Compiler.
# - entry: The logo code is received by argument 1 from the command line and
# 	is no longer contained in the "logo.txt" text file as in the original example.
# - Output: Print the binary code compiled on many levels.
# 	symbolic and current binaries.
#
# Requires the PLY package (http://www.dabeaz.com/ply)
# =================================================================================

if __name__ == '__main__':

    # ======================================
    # Read the input program
    # ======================================
    
    logoString = sys.argv[1]
    
    compiler = tinkerLogo()
    compiler.compile(logoString)

    # retrieve the byte code for use elsewhere in your code
    outputByteCode = compiler.byteCode()
